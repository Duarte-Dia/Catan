/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package catan;

/**
 *  Classe que representa uma cidade
 * @author José Sampaio
 * 
 */
public class City {

    private Vector3 position;

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public City(Vector3 position) {
        this.position = position;
    }
}
