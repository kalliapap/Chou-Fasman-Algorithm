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
public class BetaSheet {
    /*
    * Structure for beta sheet. Keeps the amino acid of the sequence
    * and for each amino acid variable isBeta is E if amino acid belongs in
    * beta sheet else - and there are two int pa,pb for the relative parameters
    */
    
    private char amino;
    private char isBeta;
    private int pa, pb;
    
    public static List<BetaSheet> betaRegion = new ArrayList<BetaSheet>();
    int countL = 0, countR = 0;

    public char getAmino() {
        return amino;
    }

    public char isIsBeta() {
        return isBeta;
    }
    //initialize list
    public void initBetaList(String sequence,List<ResidueParam> resParamList){
        for(int i=0;i<sequence.length();i++){
            BetaSheet b = new BetaSheet();
            b.amino = sequence.charAt(i);
            b.isBeta = '-';
            for(int j=0;j<resParamList.size();j++){
                if(resParamList.get(j).getName().equals(Character.toString(b.amino))){
                    b.pa = resParamList.get(j).getPa();
                    b.pb = resParamList.get(j).getPb();
                 }
             }
            betaRegion.add(i, b);     
        }
    }
    
    public void betaListToString(){
        System.out.println();
        for(int i=0; i<betaRegion.size();i++){
            System.out.print(betaRegion.get(i).isBeta);
        }
        System.out.println();
    }
    
    public List<BetaSheet> findBetaSheetRegions(){
       int window = 5;
       int countAa = 0;
       float averagePa = 0, averagePb = 0;
       int startWin, endWin, retLeft, retRight;
       for(int i = 0;i<betaRegion.size() - 4;i++){
            countAa = 0;
            //check window, 5aa for pb>100
            for(int j=i;j<i+window;j++){
                if(betaRegion.get(j).pb > 100){
                    countAa++;
                }
            }
            if(countAa >= 3){
                //if 3 then is beta
                for(int j=i;j<i+window;j++){
                    betaRegion.get(j).isBeta = 'E';
                }
                startWin = i; 
                endWin = i+window-1;
                //Extend Left
                if(startWin - 1 > 0){
                    countL = 0; averagePa = 0; averagePb = 0;
                    retLeft = extendLeft(startWin);
                    //average value of residues that extended
                    for(int j = i-1;j >= i-retLeft;j--){
                        averagePa += betaRegion.get(j).pa;
                        averagePb += betaRegion.get(j).pb;
                    }
                    averagePa = averagePa / retLeft;
                    averagePb = averagePb / retLeft;
                    //check if extension belongs to beta sheet
                    if(averagePb > 105 && averagePb > averagePa){
                        for(int j = i-1;j >= i-retLeft;j--){
                            betaRegion.get(j).isBeta = 'E';
                        }
                    }else{
                        for(int j = i-1;j >= i-retLeft;j--){
                            betaRegion.get(j).isBeta = '-';
                        }
                    }
                }
                
                //Extend Right
                if(endWin+1 < betaRegion.size()){
                    countR = 0; averagePa = 0; averagePb = 0;
                    retRight = extendRight(endWin);
                    for(int j = endWin+1;j <= endWin+retRight;j++){
                        averagePa += betaRegion.get(j).pa;
                        averagePb += betaRegion.get(j).pb;
                    }
                    averagePa = averagePa / retRight;
                    averagePb = averagePb / retRight;
                    //check if beta or extend
                    if(averagePb > 105 && averagePb > averagePa){
                        for(int j = endWin+1;j <= endWin+retRight;j++){
                            betaRegion.get(j).isBeta = 'E'; 
                        }
                        i = endWin + retRight; 
                    }else{
                        for(int j = endWin+1;j <= endWin+retRight;j++){
                            betaRegion.get(j).isBeta = '-';
                        }
                        i = endWin;
                    } 
                } 
            }   
       }
       return betaRegion;
    }
    
    public int extendLeft(int startWin){
        if(betaRegion.get(startWin - 1).isBeta == '-'){
            float average = (betaRegion.get(startWin-1).pb + betaRegion.get(startWin).pb 
                    +betaRegion.get(startWin+1).pb + betaRegion.get(startWin+2).pb)/4;
            //if true continue left
            if(average >= 100){
                startWin--;
                countL++;
                if(startWin > 1)
                    extendLeft(startWin);
                else
                    return countL;
            }
        }
        
        return countL;
    }
    
    public int extendRight(int endWin){
        float average = (betaRegion.get(endWin-1).pb+ betaRegion.get(endWin-2).pb 
                +betaRegion.get(endWin).pb+ betaRegion.get(endWin+1).pb)/4;
        if(average >= 100){
            endWin++;
            countR++;
            if(endWin < betaRegion.size()-1)
                extendRight(endWin);
            else
                return countR;
        }
        return countR;
    }

    
}
