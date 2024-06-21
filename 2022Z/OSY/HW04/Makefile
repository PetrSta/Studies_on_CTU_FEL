CFLAGS = -I -Wall -Werror $(EXTRA_CFLAGS)
CC = gcc

make: nsd forkpipe

run: forkpipe
	./forkpipe

forkpipe: forkpipe.c
	$(CC) -o forkpipe forkpipe.c $(CFLAGS)

nsd: nd.c nsd.c nsd_main.c nd.h nsd.h
	$(CC) -o nsd nd.c nsd.c nsd_main.c $(CFLAGS)

clean:
	rm -f forkpipe nsd osy04.tgz

zip:
	tar czf osy04.tgz Makefile *.[ch]