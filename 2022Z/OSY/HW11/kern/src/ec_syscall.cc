#include "ec.h"
#include "kalloc.h"
#include "ptab.h"
#include "bits.h"
#include "memory.h"
#include "string.h"

typedef enum {
    sys_print = 1,
    sys_sum = 2,
    sys_break = 3,
    sys_thr_create = 4,
    sys_thr_yield = 5,
} Syscall_numbers;

void Ec::syscall_handler (uint8 a) {
    // Get access to registers stored during entering the system - see
    // entry_sysenter in entry.S
    Sys_regs * r = current->sys_regs();
    Syscall_numbers number = static_cast<Syscall_numbers> (a);

    switch (number) {
        case sys_print: {
            // Tisk řetězce na sériovou linku
            char *data = reinterpret_cast<char*>(r->esi);
            unsigned len = r->edi;
            for (unsigned i = 0; i < len; i++) {
                printf("%c", data[i]);
            }
            break;
        }
        case sys_sum: {
            // Naprosto nepotřebné systémové volání na sečtení dvou čísel
            int first_number = r->esi;
            int second_number = r->edi;
            r->eax = first_number + second_number;
            break;
        }
        //work with ptab.cc & kalloc.cc, some useful information found in ec.cc
        //our implementation of nbrk
        case sys_break:  {
            bool canAlloc = true;
            //mword maxAddress = 0xC0000000;
            mword address = r->esi;
            mword attributes;
            mword phys;
            mword virt;

            //default return value
            r->eax = Ec::break_current;

            //just return current break
            if(address == 0) {
                printf("Request for current break value.\n");
                break;
            }

            if((address < Ec::break_min) || (address > LINK_ADDR)) {
                printf("New brk is out of allowed space.\n");
                r->eax = 0;
                break;
            }

            //requesting more memory
            if(address > Ec::break_current) {
                virt = align_up(Ec::break_current, PAGE_SIZE);
                attributes = Ptab::PRESENT | Ptab::USER | Ptab::RW;
                while(virt < address) {
                    void *page = Kalloc::allocator.alloc_page (1, Kalloc::FILL_0);
                    if(!page) {
                        printf("alloc_page failed\n");
                        canAlloc = false;
                        //deallocate is done in later if -> line 92
                        r->eax = 0;
                        break;
                    }
                    phys = Kalloc::virt2phys(page);
                    if (!Ptab::insert_mapping(virt, phys, attributes)) {
                        printf("insert_mapping failed\n");
                        canAlloc = false;
                        //deallocate is done in later if -> line 92
                        r->eax = 0;
                        break;
                    }
                    virt += PAGE_SIZE;
                }
                if(canAlloc) {
                    memset(reinterpret_cast<void*> (Ec::break_current), Kalloc::NOFILL, address - Ec::break_current);
                }
            } 
            //less memory requested -> deallocating
            if (address < Ec::break_current || !canAlloc) {
                //setup variables base on situation in which we got to this if
                mword deallocateTill;
                if(canAlloc) {
                    //address was < Ec::break_current
                    printf("Lowering break");
                    deallocateTill = align_up(address, PAGE_SIZE);
                    virt = align_up(Ec::break_current, PAGE_SIZE);
                } else {
                    //error ocured
                    deallocateTill = align_up(Ec::break_current, PAGE_SIZE);
                    virt = align_up(virt, PAGE_SIZE);
                }

                while(virt > deallocateTill) {
                    virt -= PAGE_SIZE;
                    if((phys = Ptab::get_mapping(virt) & ~PAGE_MASK) == 0) {
                        printf("get_mapping error during deallocate\n");
                        r->eax = 0;
                        break;
                    } 
                    attributes = ~Ptab::PRESENT | Ptab::USER | Ptab::RW;
                    if(!Ptab::insert_mapping(virt, phys, attributes)) {
                        printf("insert_mapping error during deallocate\n");
                        r->eax = 0;
                        break;
                    }
                    printf("Free_page\n");
                    Kalloc::allocator.free_page(Kalloc::phys2virt(phys));
                    
                }
            }
            //if we finished succesfully set new break
            if(canAlloc) {
                Ec::break_current = address;
            }
            break;
        }
        default:
            printf ("unknown syscall %d\n", number);
            break;
    };
    ret_user_sysexit();
}
