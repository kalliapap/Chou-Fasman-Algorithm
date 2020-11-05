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
public class Turn extends ResidueParam {
    private char isTurn;
    private char amino;
    public static List<Turn> turnRegion = new ArrayList<Turn>();
    
    public Turn(){
        super();
        isTurn = '-';
    }

    public char isIsTurn() {
        return isTurn;
    }
    
    public void initTurnList(String sequence,List<ResidueParam> resParamList){
        //dhmiourgia listas me aparaithta pedia gia euresh strofwn
        for(int i=0;i<sequence.length();i++){
            Turn t = new Turn();
            t.amino = sequence.charAt(i);
            t.isTurn = '-';
            for(int j=0;j<resParamList.size();j++){
                if(resParamList.get(j).getName().equals(Character.toString(t.amino))){
                    t.setPa(resParamList.get(j).getPa());
                    t.setPb(resParamList.get(j).getPb());
                    t.setPt(resParamList.get(j).getPt());
                    t.setFi(resParamList.get(j).getFi());
                    t.setFi1(resParamList.get(j).getFi1());
                    t.setFi2(resParamList.get(j).getFi2());
                    t.setFi3(resParamList.get(j).getFi3());
                 }
             }
            turnRegion.add(i, t); 
        }
    }
    
    public void turnListToString(){
        System.out.println();
        for(int i=0; i<turnRegion.size();i++){
            System.out.print(turnRegion.get(i).isTurn); 
        }
        System.out.println();
    }
    
    public List<Turn> findTurnRegions(){
        //elegxos sunthikwn gia na uparxei strofh
        for(int i=0;i<turnRegion.size()-3;i++){
            double pt,averagePt = 0,averagePa = 0,averagePb = 0;
            pt = turnRegion.get(i).getFi()*turnRegion.get(i+1).getFi1()*
                    turnRegion.get(i+2).getFi2()*turnRegion.get(i+3).getFi3();
            averagePt = (turnRegion.get(i).getPt()+turnRegion.get(i+1).getPt()+
                    turnRegion.get(i+2).getPt()+turnRegion.get(i+3).getPt())/4;
            averagePa = (turnRegion.get(i).getPa()+turnRegion.get(i+1).getPa()+
                    turnRegion.get(i+2).getPa()+turnRegion.get(i+3).getPa())/4;
            averagePb = (turnRegion.get(i).getPb()+turnRegion.get(i+1).getPb()+
                    turnRegion.get(i+2).getPb()+turnRegion.get(i+3).getPb())/4;
            if(checkConditions(pt,averagePt,averagePa,averagePb)){
                turnRegion.get(i).isTurn = 'T';
                turnRegion.get(i+1).isTurn = 'T';
                turnRegion.get(i+2).isTurn = 'T';
                turnRegion.get(i+3).isTurn = 'T';
                i+=3;
            }
            
        }
        return turnRegion;
    }
     //sunarthsh pou kanei ton elegxo twn sunthikwn kai epistrefei 0 h 1 analoga
    public boolean checkConditions(double pt,double avPt,double avPa,double avPb){
        boolean flag = false;
        if(pt > 0.000075 && avPt > 100 && avPa < avPt && avPt > avPb )
            flag = true;      
        return flag;
    }
     
    
}
