/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.Serializable;

/**
 *
 * @author Motaz
 */
public class NewMove implements Serializable{ // to be read as an object
        public int move;
        public NewMove (int m){
                move = m;
        }
}
