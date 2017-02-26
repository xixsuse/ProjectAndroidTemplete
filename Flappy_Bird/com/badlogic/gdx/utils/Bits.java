package com.badlogic.gdx.utils;

public class Bits {
    long[] bits;

    public Bits() {
        this.bits = new long[]{0};
    }

    public Bits(int nbits) {
        this.bits = new long[]{0};
        checkCapacity(nbits >>> 6);
    }

    public boolean get(int index) {
        int word = index >>> 6;
        if (word < this.bits.length && (this.bits[word] & (1 << (index & 63))) != 0) {
            return true;
        }
        return false;
    }

    public boolean getAndClear(int index) {
        int word = index >>> 6;
        if (word >= this.bits.length) {
            return false;
        }
        long oldBits = this.bits[word];
        long[] jArr = this.bits;
        jArr[word] = jArr[word] & ((1 << (index & 63)) ^ -1);
        if (this.bits[word] != oldBits) {
            return true;
        }
        return false;
    }

    public boolean getAndSet(int index) {
        int word = index >>> 6;
        checkCapacity(word);
        long oldBits = this.bits[word];
        long[] jArr = this.bits;
        jArr[word] = jArr[word] | (1 << (index & 63));
        return this.bits[word] == oldBits;
    }

    public void set(int index) {
        int word = index >>> 6;
        checkCapacity(word);
        long[] jArr = this.bits;
        jArr[word] = jArr[word] | (1 << (index & 63));
    }

    public void flip(int index) {
        int word = index >>> 6;
        checkCapacity(word);
        long[] jArr = this.bits;
        jArr[word] = jArr[word] ^ (1 << (index & 63));
    }

    private void checkCapacity(int len) {
        if (len >= this.bits.length) {
            long[] newBits = new long[(len + 1)];
            System.arraycopy(this.bits, 0, newBits, 0, this.bits.length);
            this.bits = newBits;
        }
    }

    public void clear(int index) {
        int word = index >>> 6;
        if (word < this.bits.length) {
            long[] jArr = this.bits;
            jArr[word] = jArr[word] & ((1 << (index & 63)) ^ -1);
        }
    }

    public void clear() {
        long[] bits = this.bits;
        int length = bits.length;
        for (int i = 0; i < length; i++) {
            bits[i] = 0;
        }
    }

    public int numBits() {
        return this.bits.length << 6;
    }

    public int length() {
        long[] bits = this.bits;
        for (int word = bits.length - 1; word >= 0; word--) {
            long bitsAtWord = bits[word];
            if (bitsAtWord != 0) {
                for (int bit = 63; bit >= 0; bit--) {
                    if (((1 << (bit & 63)) & bitsAtWord) != 0) {
                        return (word << 6) + bit;
                    }
                }
                continue;
            }
        }
        return 0;
    }

    public boolean isEmpty() {
        for (long j : this.bits) {
            if (j != 0) {
                return false;
            }
        }
        return true;
    }

    public int nextSetBit(int fromIndex) {
        long[] bits = this.bits;
        int word = fromIndex >>> 6;
        int bitsLength = bits.length;
        if (word >= bitsLength) {
            return -1;
        }
        int i;
        long bitsAtWord = bits[word];
        if (bitsAtWord != 0) {
            for (i = fromIndex & 63; i < 64; i++) {
                if (((1 << (i & 63)) & bitsAtWord) != 0) {
                    return (word << 6) + i;
                }
            }
        }
        for (word++; word < bitsLength; word++) {
            if (word != 0) {
                bitsAtWord = bits[word];
                if (bitsAtWord != 0) {
                    for (i = 0; i < 64; i++) {
                        if (((1 << (i & 63)) & bitsAtWord) != 0) {
                            return (word << 6) + i;
                        }
                    }
                    continue;
                } else {
                    continue;
                }
            }
        }
        return -1;
    }

    public int nextClearBit(int fromIndex) {
        long[] bits = this.bits;
        int word = fromIndex >>> 6;
        int bitsLength = bits.length;
        if (word >= bitsLength) {
            return -1;
        }
        int i;
        long bitsAtWord = bits[word];
        for (i = fromIndex & 63; i < 64; i++) {
            if (((1 << (i & 63)) & bitsAtWord) == 0) {
                return (word << 6) + i;
            }
        }
        for (word++; word < bitsLength; word++) {
            if (word == 0) {
                return word << 6;
            }
            bitsAtWord = bits[word];
            for (i = 0; i < 64; i++) {
                if (((1 << (i & 63)) & bitsAtWord) == 0) {
                    return (word << 6) + i;
                }
            }
        }
        return -1;
    }

    public void and(Bits other) {
        int i = 0;
        int j = this.bits.length;
        int k = other.bits.length;
        while (i < j && i < k) {
            long[] jArr = this.bits;
            jArr[i] = jArr[i] & other.bits[i];
            i++;
        }
    }

    public void andNot(Bits other) {
        int i = 0;
        int j = this.bits.length;
        int k = other.bits.length;
        while (i < j && i < k) {
            long[] jArr = this.bits;
            jArr[i] = jArr[i] & (other.bits[i] ^ -1);
            i++;
        }
    }

    public void or(Bits other) {
        int i = 0;
        int j = this.bits.length;
        int k = other.bits.length;
        while (i < j && i < k) {
            long[] jArr = this.bits;
            jArr[i] = jArr[i] | other.bits[i];
            i++;
        }
    }

    public void xor(Bits other) {
        int i = 0;
        int j = this.bits.length;
        int k = other.bits.length;
        while (i < j && i < k) {
            long[] jArr = this.bits;
            jArr[i] = jArr[i] ^ other.bits[i];
            i++;
        }
    }

    public boolean intersects(Bits other) {
        long[] bits = this.bits;
        long[] otherBits = other.bits;
        for (int i = Math.min(bits.length, otherBits.length) - 1; i >= 0; i--) {
            if ((bits[i] & otherBits[i]) != 0) {
                return true;
            }
        }
        return false;
    }

    public boolean containsAll(Bits other) {
        int i;
        long[] bits = this.bits;
        long[] otherBits = other.bits;
        int otherBitsLength = otherBits.length;
        int bitsLength = bits.length;
        for (i = bitsLength; i < otherBitsLength; i++) {
            if (otherBits[i] != 0) {
                return false;
            }
        }
        for (i = Math.min(bitsLength, otherBitsLength) - 1; i >= 0; i--) {
            if ((bits[i] & otherBits[i]) != otherBits[i]) {
                return false;
            }
        }
        return true;
    }
}
