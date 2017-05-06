package com.alex.mathlogic.common.node;

import com.alex.mathlogic.common.Utils;
import com.alex.mathlogic.common.math.HashCodeHelper;

import java.util.Objects;

/**
 * @author Alexey Katsman
 * @since 26.10.16
 */

public class Node {

    private String stringNotation;
    private Node left;
    private Node right;
    private int verticesQuantity = 1;
    private final int hash;

    public Node(String stringNotation) {
        this(stringNotation, null, null);
    }

    public Node(String stringNotation, Node left, Node right) {
        this.stringNotation = stringNotation;
        this.left = left;
        this.right = right;

        if (left != null) {
            verticesQuantity += left.verticesQuantity;
        }

        if (right != null) {
            verticesQuantity += right.verticesQuantity;
        }

        hash = calculateHash();
    }

    public String getStringNotation() {
        return stringNotation;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public String getRecursiveStringNotation() {
        StringBuilder result = new StringBuilder();

        if (left != null) {
            result.append(left.getEmbeddedStringNotation());
        }

        result.append(stringNotation);

        if (right != null) {
            result.append(right.getEmbeddedStringNotation());
        }

        return result.toString();
    }

    private String getEmbeddedStringNotation() {
        return Utils.isVariable(stringNotation) ? stringNotation : "(" + getRecursiveStringNotation() + ')';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        if (hashCode() != obj.hashCode()) {
            return false;
        }

        Node other = (Node) obj;

        return Objects.equals(stringNotation, other.stringNotation)
            && Objects.equals(left, other.left)
            && Objects.equals(right, other.right);
    }

    private int calculateHash() {
        int hash = 0;
        hash += left == null ? 0 : left.hashCode();
        hash *= 31;
        hash += stringNotation.hashCode();

        if (right != null) {
            hash *= HashCodeHelper.getPrime31(right.verticesQuantity);
            hash += right.hashCode();
        }

        return hash;
    }

    @Override
    public int hashCode() {
        return hash;
    }
}
