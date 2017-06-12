package ujm.wi.m1.algo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import weka.core.Utils;

public class NeuralNet {
	/*
	
	MultilayerPerceptron multiLayerPer;

    public NeuralNet() {
        this.multiLayerPer = init();
    }
    
    public MultilayerPerceptron init(){
        File inputFile = new File(FichierConstante.SAVE_RDN);
        System.out.println("Initialisation Reseau de Neurones.");
        
        if(inputFile.isFile()){
            return this.load(inputFile);
        }
        else{
            System.out.println("Nouveau Reseau.");
            MultilayerPerceptron mlp = new MultilayerPerceptron();
            return mlp;
        }
    }

    public void training() {
        try {
            FileReader trainReader = new FileReader(Writer.verifFichier(FichierConstante.FICHIER_ARFF_TRAIN));
            Instances train = new Instances(trainReader);
            train.setClassIndex(train.numAttributes() - 1);
            
            System.out.println("Training avec option : " + AlgosConstantes.RDN_OPTIONS);

            rdn.setOptions(Utils.splitOptions(AlgosConstantes.RDN_OPTIONS));
            rdn.buildClassifier(train);
            
            sauverRdN();
            
            trainReader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReseauNeurones.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReseauNeurones.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ReseauNeurones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int tester() {
        try {
            FileReader validReader = new FileReader(Writer.verifFichier(FichierConstante.FICHIER_ARFF_VALID));
            Instances valid = new Instances(validReader);
            valid.setClassIndex(valid.numAttributes() - 1);

            validReader.close();

            return (int)Math.round(rdn.classifyInstance(valid.lastInstance()));
        } catch (Exception ex) {
            Logger.getLogger(ReseauNeurones.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    
    private MultilayerPerceptron load(File inputFile) throws IOException, ClassNotFoundException {
        if (inputFile.isFile()) {          
                FileInputStream save = new FileInputStream(inputFile);
                ObjectInputStream ois = new ObjectInputStream(save);
                System.out.println("Chargement Reseau de Neurones.");
                MultilayerPerceptron rdn = (MultilayerPerceptron) ois.readObject();
                ois.close();
                return rdn;          
        }       
        return null;
    }

    private void sauverRdN() {
        File ficRdN = Writer.verifFichier(FichierConstante.SAVE_RDN);
        
        try {
            FileOutputStream save = new FileOutputStream(ficRdN);
            ObjectOutputStream oos = new ObjectOutputStream(save);
            
            System.out.println("Sauvegarde Reseau de Neurones.");
            oos.writeObject(rdn);

            oos.flush();
            oos.close();
        } catch (IOException ex) {
            Logger.getLogger(ReseauNeurones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

	 */
	
}
