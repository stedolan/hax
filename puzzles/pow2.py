#!/usr/bin/env python2

# Why does this print 2**n (for n > 0)?

def pow2(n):
    return len(eval(n*'`'+`''`+'`'*n))

n = 16
print(pow2(n))
