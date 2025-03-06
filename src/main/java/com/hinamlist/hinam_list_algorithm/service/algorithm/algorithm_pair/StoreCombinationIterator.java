package com.hinamlist.hinam_list_algorithm.service.algorithm.algorithm_pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class StoreCombinationIterator implements Iterator<List<Integer>> {
    private final int i;
    private final int j;
    private final int size;
    private final int totalCombinations;
    private int currentCombination;

    public StoreCombinationIterator(int i, int j, int size) {
        this.i = i;
        this.j = j;
        this.size = size;

        // Total number of combinations is 2^size
        this.totalCombinations = 1 << size;
        this.currentCombination = 0;
    }

    @Override
    public boolean hasNext() {
        return currentCombination < totalCombinations;
    }

    @Override
    public List<Integer> next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more combinations.");
        }

        List<Integer> combination = new ArrayList<>(size);
        for (int bit = 0; bit < size; bit++) {
            // Use bitwise AND to determine which number to use
            combination.add(((currentCombination >> bit) & 1) == 1 ? j : i);
        }

        currentCombination++;

        return combination;
    }
}

