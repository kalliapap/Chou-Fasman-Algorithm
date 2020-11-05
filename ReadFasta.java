/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package choufasmanalgorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import static java.lang.System.exit;

/**
 *
 * @author Kalliopi
 */
public class ReadFasta {
    private String header;
    private String sequence;
    
    public String getHeader(File file){
        readFastaFile(file);
        return header;
    }
    public String getSequence(File file){
        readFastaFile(file);
        return sequence;
    }
    //diavazei to arxeio se morfh fasta an den einai se fasta format termatizei 
    //me mhnuma sto xrhsth
    private void readFastaFile(File fastaFile) {
        InputStream fasta;
        String line;
        try {
            fasta = new FileInputStream(fastaFile);
            InputStreamReader inputStream = new InputStreamReader(fasta);
            BufferedReader buff = new BufferedReader(inputStream);
            int lineNb = 0;
            StringBuilder sb = new StringBuilder();
            while ((line = buff.readLine()) != null){
                if(!line.startsWith(">", 0) && lineNb == 0){
                    System.out.println("File is not in FASTA format");
                    exit(-1);
                }
                if(line.startsWith(">", 0)){
                    if (lineNb == 0) {
                        this.header = line; //apothikeush header
                    }
                }else{
                    sb.append(line);
                }
                lineNb++;
            }
            this.sequence = sb.toString(); //apothikeush akolouthias
            buff.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
