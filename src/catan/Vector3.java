/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package catan;

/**
 *  Classe que representa um vector de três dimensões
 * @author Bruno Ribeiro
 */
public class Vector3 {
    public int x, y, z;
    
    public Vector3(){
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }
    
    public Vector3(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
