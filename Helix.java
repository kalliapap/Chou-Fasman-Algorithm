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
public class Helix {
    private char amino;
    private char isHelix;
    private int pa, pb;
    
    public static List<Helix> helixRegion = new ArrayList<Helix>();
    int countL = 0, countR = 0;

    public char getAmino() {
        return amino;
    }

    public char isIsHelix() {
        return isHelix;
    }

    public int getPa() {
        return pa;
    }

    public int getPb() {
        return pb;
    }
    
    public void initHelixList(String sequence,List<ResidueParam> resParamList){
        for(int i=0;i<sequence.length();i++){
            Helix h = new Helix();
             h.amino = sequence.charAt(i);
             h.isHelix = '-';
             for(int j=0;j<resParamList.size();j++){
                 if(resParamList.get(j).getName().equals(Character.toString(h.amino))){
                     h.pa = resParamList.get(j).getPa();
                     h.pb = resParamList.get(j).getPb();
                 }
             }
            helixRegion.add(i, h); 
        }
    }
    
    public void helixListToString(){
        System.out.println();
        for(int i=0; i<helixRegion.size();i++){
            System.out.print(helixRegion.get(i).amino);
        }
        System.out.println();
        for(int i=0; i<helixRegion.size();i++){
            System.out.print(helixRegion.get(i).isHelix);
        }
        System.out.println();
    }
    
    public List<Helix> findHelicalRegions(){
        int window = 6, countAa = 0;
        int startWin, endWin;
        int retLeft, retRight = 0;
        float averagePa = 0, averagePb = 0;
        for(int i=0;i<helixRegion.size()-6;i++){
            countAa = 0;
            //check window for aa that belogs to helix
            for(int j=i;j<i+window;j++){
                if(helixRegion.get(j).pa > 100){
                    countAa++;
                }
            }
            if(countAa >= 4){
                //If more than 4aa have pa > 100 then is helix
                for(int j=i;j<i+window;j++){
                    helixRegion.get(j).isHelix = 'H';
                }
                startWin = i; 
                endWin = i+window-1; //end of extendRight
                //Extend Left
                if(startWin - 1 > 0 && helixRegion.get(startWin-1).isHelix == '-'){
                    averagePa = 0; averagePb = 0; countL = 0; 
                    retLeft = extendLeft(startWin);
                    
                    if(retLeft > 5 && i-retLeft >= 0){
                        for(int k = i-1;k >= i-retLeft;k--){
                            averagePa += helixRegion.get(k).pa;
                            averagePb += helixRegion.get(k).pb;
                        }
                        averagePa = averagePa/retLeft;
                        averagePb = averagePb/retLeft;
                        //check if extend belongs to helix
                        if(averagePa > averagePb){
                            for(int k = i-1;k >= i-retLeft;k--){
                                helixRegion.get(k).isHelix = 'H';
                            }
                        }else{
                            for(int k = i-1;k >= i-retLeft;k--){
                                helixRegion.get(k).isHelix = '-';
                            }
                        }
                        //if extend <= 5aa then belongs to helix
                    }else if(retLeft <= 5){
                        for(int k = i-1;k >= i-retLeft;k--){
                            helixRegion.get(k).isHelix = 'H';
                        }
                    }
                }
                //Extend Right
                if(endWin+1 < helixRegion.size()){
                    averagePa = 0; averagePb = 0; countR = 0;
                    retRight = extendRight(endWin);
                    if(retRight > 5 && endWin+retRight <= helixRegion.size()){
                        for(int k = endWin+1;k <= endWin+retRight;k++){
                            averagePa += helixRegion.get(k).pa;
                            averagePb += helixRegion.get(k).pb;
                        }
                        averagePa = averagePa/retRight;
                        averagePb = averagePb/retRight;
                        if(averagePa > averagePb){
                            for(int k = endWin+1;k <= endWin+retRight;k++){
                                helixRegion.get(k).isHelix = 'H';
                            }
                            i=endWin + retRight;
                        }else{
                            for(int k = endWin+1;k <= endWin+retRight;k++){
                                helixRegion.get(k).isHelix = '-';
                            }
                            i = endWin;
                        }
                        
                    }else if(retRight <= 5){
                        for(int k = endWin+1;k <= endWin+retRight;k++){
                            helixRegion.get(k).isHelix = 'H';
                        }
                        i=endWin + retRight;
                    }
                }
            }
        }
        return helixRegion;
    }
    
    public int extendLeft(int startWin){
            float average = (helixRegion.get(startWin-1).pa + helixRegion.get(startWin).pa 
                    +helixRegion.get(startWin+1).pa + helixRegion.get(startWin+2).pa)/4;
            if(average >= 100){
                startWin--;
                countL++;
                if(startWin > 1)
                    extendLeft(startWin);
                else
                    return countL;
            }
        return countL;
    }
    
    public int extendRight(int endWin){
            float average = (helixRegion.get(endWin-1).pa + helixRegion.get(endWin-2).pa 
                    +helixRegion.get(endWin).pa + helixRegion.get(endWin+1).pa)/4;
            if(average >= 100){
                endWin++;
                countR++;
                if(endWin < helixRegion.size()-1)
                    extendRight(endWin);
                else{
                   // System.out.println("End of sequence. Returned from extendRight()");
                    return countR;
                }
            }
        return countR;
    }
}
