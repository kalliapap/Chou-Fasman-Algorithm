/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package choufasmanalgorithm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.SwingUtilities;

/**
 *
 * @author Kalliopi
 */
public class ChouFasmanAlgorithm {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {    
          
       System.out.println("Please give filename");
       Scanner scan = new Scanner(System.in);
       String in = scan.nextLine();
       File input = new File(in);
       ReadFasta rf = new ReadFasta();
       String sequence = rf.getSequence(input);
       
       System.out.println("Input Sequence: "+sequence );
       
       
       ResidueParam resP = new ResidueParam();
       List<ResidueParam> resParamList = new ArrayList<ResidueParam>();
       resParamList = resP.readParametersFile();
       resP.toPrint();
       
       List<Helix> helix = new ArrayList<Helix>();
       Helix h = new Helix();
       h.initHelixList(sequence,resParamList);
       helix = h.findHelicalRegions();
       h.helixListToString();
      
       List<BetaSheet> beta = new ArrayList<BetaSheet>();
       BetaSheet b = new BetaSheet();
       b.initBetaList(sequence,resParamList);
       beta = b.findBetaSheetRegions();
       b.betaListToString();
       
       List<Turn> turn = new ArrayList<Turn>();
       Turn t = new Turn();
       t.initTurnList(sequence, resParamList);
       turn = t.findTurnRegions();
       t.turnListToString();
       
       List<Total> total = new ArrayList<Total>();
       Total tot = new Total();
       total = tot.totalList(helix, beta, turn);
       total = tot.findConsesus(total);
       tot.totalListToString(total);
       
        //calculation of total amino acids for each structure(helix,beta-sheet,turn)
        //in the consensus sequence
        float helices = 0,betas = 0,turns = 0; 
        for(int i = 0; i<total.size();i++){
            if(total.get(i).getConsensus() == 'H')
                helices++;
            else if(total.get(i).getConsensus() == 'E')
                betas++;
            else if(total.get(i).getConsensus() == 'T')
                turns++;
        }
        //calculation of percentages 
        float hel,bet,tur;
        hel = helices/sequence.length()*100;
        bet = betas/sequence.length()*100;
        tur = turns/sequence.length()*100;
        System.out.println("Helices: "+helices+"\tBeta: "+betas+"\tTurns: "+turns);
       
       // Write to File
        String header = rf.getHeader(input);
        String[] parts;
        String name;
        parts = header.split("\\:");
        name = parts[0].substring(1);
       
       String out = findOutput(sequence,helix,beta,turn,total);
        System.out.println(out);
        
        try{
            File output = new File("choufasman.txt");
            FileOutputStream fos = new FileOutputStream(output);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            
            bw.write("\tChou & Fasman Algorithm Results for Protein "+header);
            bw.newLine();
            bw.write("\t=======================================================");
            bw.newLine();
            bw.newLine();
            int k = 0;
            bw.write(out+"\n");
            bw.write("Total residues\t\tHelices: "+(int)helices+"\tBeta: "+(int)betas+"\tTurns: "+(int)turns);
            bw.newLine();
            bw.write("Percentage:\t\t\t\t"+hel+"\t\t"+bet+"\t\t"+tur);          
            
            bw.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
        
        double[] pa = new double[helix.size()];
        double[] pb = new double[helix.size()];
        for(int i =0;i<pa.length-3;i++){
            pa[i] = ((double)helix.get(i).getPa()+(double)helix.get(i+1).getPa()+(double)helix.get(i+2).getPa()+(double)helix.get(i+3).getPa())/4;
            pb[i] = ((double)helix.get(i).getPb()+(double)helix.get(i+1).getPb()+(double)helix.get(i+2).getPb()+(double)helix.get(i+3).getPb())/4;
        }
        SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
             
            Diagram.createAndShowGui(pa,pb,sequence);
         }
      });

    
    }
    /*
    * Makes the final output string. Alignes every 60 amino acids the output
    */
    public static String findOutput(String sequence,List<Helix> helix,List<BetaSheet> beta,
       List<Turn> turn, List<Total> total){
       int totalCount,toRem=0;
       String out="",fin ="";
       if(sequence.length()%60==0){
           totalCount = sequence.length()/60;
       }else{
           totalCount = sequence.length()/60+1;
       }
       int start = 0;
        for(int i=0;i<totalCount;i++){
            out = "";
            //sequences separated every 60aa
            out += "S:\t";
            for(int j = start;j<(toRem+60);j++){
                if(j < sequence.length()){
                    out += sequence.charAt(j);   
                }
            }
            out += "\n";
            //helix separated every 60aa
            out += "H:\t";
            for(int j = start;j<(toRem+60);j++){
                if(j < helix.size()){
                    out += helix.get(j).isIsHelix();   
                }
            }
            out += "\n";
            out += "B:\t";
            //beta sheet separated every 60aa
            for(int j = start;j<(toRem+60);j++){
                if(j < beta.size()){
                    out += beta.get(j).isIsBeta();   
                }
            }
            out += "\n";
            out += "T:\t";
            //turn separated every 60aa
            for(int j = start;j<(toRem+60);j++){
                if(j < turn.size()){
                    out += turn.get(j).isIsTurn();   
                }
            }
            out += "\n";
            out += "F:\t";
            //consensus separated every 60aa
            for(int j = start;j<(toRem+60);j++){
                if(j < total.size()){
                    out += total.get(j).getConsensus();   
                }
            }
            out += "\n";
            start = toRem+60;
            toRem = start;
            fin +="\n"+out;
        }
        return fin;
    }
}
