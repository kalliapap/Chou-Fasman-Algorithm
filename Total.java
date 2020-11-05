/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package choufasmanalgorithm;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kalliopi
 */
public class Total {
    
    private char amino;
    private char helix, beta, turn;
    private int pa, pb;
    private char consensus;
    
    public List<Total> totalList(List<Helix> helix,List<BetaSheet> beta,List<Turn> turn){
        //create list with all the fields needed for consensus sequence
        List<Total> total = new ArrayList<Total>();
        for(int i = 0;i < helix.size();i++){
            Total t = new Total();
            t.amino  = helix.get(i).getAmino();
            t.helix = helix.get(i).isIsHelix();
            t.beta = beta.get(i).isIsBeta();
            t.turn = turn.get(i).isIsTurn();
            t.pa = helix.get(i).getPa();
            t.pb = helix.get(i).getPb();
            t.consensus = '-';
            total.add(i,t);
            System.out.println(total.get(i).amino+"\t"+total.get(i).pa+"\t"+total.get(i).pb+"\t");
        }
        return total;
    }
    
    
    public List<Total> findConsesus(List<Total> total){
        int start,end;
        float averagePa=0,averagePb=0;
        for(int i=0;i<total.size();i++){
            averagePa = 0;
            averagePb = 0;
            //overlapping areas
            if(total.get(i).helix == 'H' && total.get(i).beta == 'E'){
                start = i;
                while(total.get(i).helix == 'H' && total.get(i).beta == 'E'){
                    i++;
                }
                i--;
                end = i;
                //find average
                for(int j = start;j<=end;j++){
                    averagePa += total.get(j).pa;
                    averagePb += total.get(j).pb;    
                }
                averagePa = averagePa /(end - start+1);
                averagePb = averagePb /(end - start+1);
                //check if prevails helix or beta sheet
                if(averagePa > averagePb){
                    for(int j = start;j<=end;j++){
                        total.get(j).consensus = 'H';
                    }   
                }else{
                    for(int j = start;j<=end;j++){
                        total.get(j).consensus = 'E';
                    }  
                }
            }
            //areas that contain only helices or beta sheet 
            if(total.get(i).helix == 'H' && total.get(i).beta != 'E'){
                total.get(i).consensus = 'H';   
            }
            if(total.get(i).beta == 'E' && total.get(i).helix != 'H'){
                total.get(i).consensus = 'E';    
            }       
        }  
        //turns
        for(int i=0;i<total.size();i++){
            if(total.get(i).turn == 'T'){
                total.get(i).consensus = 'T';  
            }
        }
        return total; 
    }
    
    public void totalListToString(List<Total> total){
        System.out.println();
        for(int i=0; i<total.size();i++){
            System.out.print(total.get(i).consensus);
        }
        System.out.println();
    }

    public char getConsensus() {
        return consensus;
    }
    
    
    
}
