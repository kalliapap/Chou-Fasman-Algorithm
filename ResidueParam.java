/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package choufasmanalgorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kalliopi
 */
public class ResidueParam {
    private String name;
    private int pa;
    private int pb;
    private int pt;
    private double fi;
    private double fi1;
    private double fi2;
    private double fi3;

    public ResidueParam() {
    }

    public ResidueParam(String name,int pa,int pb){
        this.name = name;
        this.pa = pa;
        this.pb = pb;
    }
    public ResidueParam(String name, int pa, int pb, int pt, double fi, double fi1, double fi2, double fi3) {
        this.name = name;
        this.pa = pa;
        this.pb = pb;
        this.pt = pt;
        this.fi = fi;
        this.fi1 = fi1;
        this.fi2 = fi2;
        this.fi3 = fi3;
    }
    
    public List<ResidueParam> readParametersFile(){
        //diavazei tis parametrous apo to arxeio param.txt
        //apothikeush parametrou se lista tupou ResidueParam
        List<ResidueParam> res = new ArrayList<ResidueParam>();
        List<ResidueParam> helix = new ArrayList<ResidueParam>();
        File input = new File("param.txt");
        
        try(BufferedReader br = new BufferedReader(new FileReader(input))){
            String line = null;
            while ((line = br.readLine()) != null) {
                    String[] param = line.split(" +");
                    int[] p = {Integer.parseInt(param[1]),Integer.parseInt(param[2]),Integer.parseInt(param[3])};
                    double[] fi = {Double.parseDouble(param[4]),Double.parseDouble(param[5]),Double.parseDouble(param[6]),Double.parseDouble(param[7])};
                    ResidueParam r = new ResidueParam(param[0],p[0],p[1],p[2],fi[0],fi[1],fi[2],fi[3]);
                    res.add(r);
            }
        }catch(IOException x){
            System.err.format("IOException\nInput file not found: %s%n", x);
        }
        return res;
    }
      
    public void toPrint(){
       List<ResidueParam> resParamList = new ArrayList<ResidueParam>();
       resParamList = readParametersFile();
       System.out.println("Name\tP(a)\tP(b)\tP(t)\tF(i)\tF(i+1)\tF(i+2)\tF(i+3)");
       for(int i =0;i<resParamList.size();i++){
           System.out.println(resParamList.get(i).getName()+"\t"+resParamList.get(i).getPa()+"\t"+
                   resParamList.get(i).getPb()+"\t"+resParamList.get(i).getPt()+"\t"+resParamList.get(i).getFi()+
                   "\t"+resParamList.get(i).getFi1()+"\t"+resParamList.get(i).getFi2()+"\t"+
                   resParamList.get(i).getFi3());
       }
    }
    
     public String getName() {
        return name;
    }
    public int getPa() {
        return pa;
    }
    public int getPb() {
        return pb;
    }
    public int getPt() {
        return pt;
    }
    public double getFi() {
        return fi;
    }
    public double getFi1() {
        return fi1;
    }
    public double getFi2() {
        return fi2;
    }
    public double getFi3() {
        return fi3;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPa(int pa) {
        this.pa = pa;
    }

    public void setPb(int pb) {
        this.pb = pb;
    }

    public void setPt(int pt) {
        this.pt = pt;
    }

    public void setFi(double fi) {
        this.fi = fi;
    }

    public void setFi1(double fi1) {
        this.fi1 = fi1;
    }

    public void setFi2(double fi2) {
        this.fi2 = fi2;
    }

    public void setFi3(double fi3) {
        this.fi3 = fi3;
    }
    
}
