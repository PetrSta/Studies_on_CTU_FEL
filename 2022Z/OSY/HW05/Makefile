CFLAGS = -Wall -g -O2 $(EXTRA_CFLAGS)
EXTRA_CFLAGS ?= -fsanitize=address -fno-omit-frame-pointer
CC = gcc

make: prod-cons

run: prod-cons
	./prod-cons

prod-cons: prod-cons.c
	$(CC) -o prod-cons prod-cons.c $(CFLAGS)

clean:
	rm -f prod-cons osy05.tgz

zip:
	tar czf osy05.tgz Makefile *.[ch]