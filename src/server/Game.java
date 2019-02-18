/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.Serializable;

/**
 *
 * @author Motaz
 */
public class Game implements Serializable{

    public String player1;
    public String player2;
    public String cell11;
    public String cell12;
    public String cell13;
    public String cell21;
    public String cell22;
    public String cell23;
    public String cell31;
    public String cell32;
    public String cell33;
    public Game(String player1, String player2, String cell11, String cell12, String cell13,
            String cell21, String cell22, String cell23, String cell31, String cell32, String cell33) {
        this.player1 = player1;
        this.player2 = player2;
        this.cell11 = cell11;
        this.cell12 = cell12;
        this.cell13 = cell13;
        this.cell21 = cell21;
        this.cell22 = cell22;
        this.cell23 = cell23;
        this.cell31 = cell31;
        this.cell32 = cell32;
        this.cell33 = cell33;
    }
}
