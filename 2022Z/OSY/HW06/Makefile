CFLAGS=-Wall -g -O2 $(EXTRA_CFLAGS)
EXTRA_CFLAGS?=-fsanitize=address -fno-omit-frame-pointer
CC=gcc

make: factory

run: factory
	./factory

factory: factory.c
	$(CC) -o factory factory.c $(CFLAGS)

clean:
	rm -f factory osy06.tgz

zip:
	tar czf osy06.tgz Makefile *.[ch]