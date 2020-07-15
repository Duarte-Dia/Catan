/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package catan;

import java.util.*;

/**
 *  Classe que define cada hexágono do tabuleiro
 * @author Bruno Ribeiro Criaçao da Classe
 */
public class Hexagon {

    private int id, num, resourceID;
    private List<Vector3> vectors = new ArrayList<Vector3>();

    public Hexagon(int id, int num, int resourceID) {
        this.id = id;
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
     /**
      * Método que recebe o vector3 e indica que as coordenadas seleccionadas são iguais às coordenadas de cada vértice do Héxagono
      * @param selectedVector Parametro que representa o vector seleccionado
      * @return Retorna verdadeiro quando o vetor seleccionado tem coordenadas iguais ás coordenadas do vértice do Hexágono
      */
    public boolean containVector(Vector3 selectedVector) {
        for (Vector3 v : vectors) {
            if (selectedVector.x == v.x && selectedVector.y == v.y && selectedVector.z == v.z) {
                return true;
            }
        }

        return false;
    }
}
