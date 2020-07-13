/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package catan;

import java.util.*;

/**
 *
 * @author Bruno Ribeiro Criaçao da Classe
 */
public class Hexagon {

    private int num, resourceID;
    private List<Vector3> vectors = new ArrayList<Vector3>();

    public Hexagon(int num, int resourceID) {
        this.num = num;
        this.resourceID = resourceID;
    }

    public int getResourceID() {
        return resourceID;
    }

    public void setResourceID(int Resource) {
        this.resourceID = resourceID;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void addVectors(Vector3 v) {
        vectors.add(v);
    }

    public boolean containVector(Vector3 selectedVector) {
        for (Vector3 v : vectors) {
            if (selectedVector.x == v.x && selectedVector.y == v.y && selectedVector.z == v.z) {
                return true;
            }
        }

        return false;
    }
}
