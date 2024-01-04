package com.cgvsu.affine;

public class AffineExceptions extends NullPointerException {
    public AffineExceptions(String message) {
        super("Exception in affine transformation: " + message);
    }
}
