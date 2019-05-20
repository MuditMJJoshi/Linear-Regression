//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title:           Linear Regression
//
// Author:          Mudit Joshi
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Class that manipulate and learn
 * the bodies and brains of the species given.
 * 
 * @author Mudit Joshi
 *
 */
public class BodyVsBrain {

	/**
	 * Reads the data file
	 * @return data
	 */
    private static List<List<String>> read(){
        List<List<String>> dataset = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new FileReader("data.txt"))) {
            String val;
            while ((val = in.readLine()) != null) {
                String[] newval = val.split(",");
                dataset.add(Arrays.asList(newval));
            }
        }
        catch(FileNotFoundException ex){
            System.out.println("File Not Found.");
        } catch (IOException e) {
			e.printStackTrace();
		}
        return dataset;
    }
    
    
    
    /**
     * Mean of the values
     * 
     * @param sum: sum 
     * @param count: count 
     * @return mean : mean
     */
    private static double meanvalue(double sum, double count) {
    	return sum / count;
    }
    
    
    /**
     * Normalize Values
     * 
     * @param x : x set
     * @param bodyMean : BodyMean
     * @param stdev : Standard Deviation
     * @return Normalized Value
     */
    private static double normalizevalue(double x, double bodyMean, double stdev) {
    	return (x - bodyMean) / stdev;
    }
    
    
    /**
     * Finds the MSE value
     * 
     * @param b1 Beta 0
     * @param b2 Beta 1
     * @param bodies bodies values
     * @param brains brains values
     * @return MSE values
     */
    private static double MSEvalues(double b0, double b1, ArrayList<Double> bodies, 
    	ArrayList<Double> brains) {
    	double sum = 0;
		for (int i = 0; i < bodies.size(); i++) {
    		double newsum = b0 + (b1 * bodies.get(i)) - brains.get(i);
    		sum += Math.pow(newsum, 2);
    	}
    	
    	return sum / bodies.size();
    }

    
    /**
     * Standard deviation of given values
     * 
     * @param mean: subtract from values 
     * @param numList: compared with mean
     * @param count: count 
     * @return standard deviation
     */
    private static double stdevvalue(double mean, ArrayList<Double> numList) {
    	double sum = 0;
    	
    	for (Double i : numList) {
    		double newsum = (i - mean);
    		sum += Math.pow(newsum, 2);
    	}
    	double var = sum / (numList.size() - 1);
    	
    	return Math.sqrt( var );
    }
    
    
    /**
     * Partial MSE
     * 
     * @param b1 Beta 0
     * @param b2 Beta 1
     * @param bodies bodies values
     * @param brains brains value
     * @return Partial MSE values
     */
    private static double partialMSEvalue(double b0, double b1,
    		ArrayList<Double> bodies, 
    	ArrayList<Double> brains, int n) {
    	double sum = 0;
    	
    	if (n != -1) {
    		double x2 = bodies.get(n);
    		double y2 = brains.get(n);
    		
    		return 2 * (b0 + (b1 * x2) - y2) * x2;
    	}
    	
    	for (int i = 0; i < bodies.size(); i++) {
    		double temp = b0 + (b1 * bodies.get(i)) - brains.get(i);
    		sum += temp * bodies.get(i);
    	}
    	
    	return 2 * sum / bodies.size();
    }
    
    /**
     * OrderLeast value for calculation
     * 
     * @param bodyMean Body Mean
     * @param brainMean Brain Mean
     * @param bodies List 
     * @param brains List
     * @return calc value
     */
    private static ArrayList<Double> Ordvalue(double bodyMean, double brainMean,
    		ArrayList<Double> bodies, ArrayList<Double> brains) {
    	ArrayList<Double> val = new ArrayList<>();
    	double nume = 0;
    	double denom = 0;
    	
    	for(int i = 0; i < bodies.size(); i++) {
    		double x1 = bodies.get(i) - bodyMean;
    		double y1 = brains.get(i) - brainMean;
    		
    		nume += (x1 * y1);
    		denom += Math.pow(x1, 2);
    	}
    	
    	double b1 = nume / denom;
        double b0 = brainMean - (b1 * bodyMean);
        double MSE = MSEvalues(b0, b1, bodies, brains);
        
        val.add(b0);
        val.add(b1);
        val.add(MSE);
    	
    	return val;
    }
    
    /**
     * Second Partial MSE
     * 
     * @param b1 b1 List
     * @param b2 b2 List
     * @param bodies List
     * @param brains List
     * @return calc value
     */
    private static double partialMSEvalue2(double b0, double b1,
    		ArrayList<Double> bodies, 
    	ArrayList<Double> brains, int n) {
    	double sum = 0;
    	
    	if (n != -1) {
    		double x1 = bodies.get(n);
    		double y1 = brains.get(n);
    		
    		return 2 * (b0 + (b1 * x1) - y1);
    	}
    	for (int i = 0; i < bodies.size(); i++) {
    		double val = b0 + (b1 * bodies.get(i)) - brains.get(i);
    		sum += val;
    	}
    	
    	return 2 * sum / bodies.size();
    }
    
   
    /**
     * 
     * Calculate grand Descent
     * 
     * @param brain List
     * @param bodies List
     * @return grand Descent
     */
    private static ArrayList<Double> gradDescent(int t, double eta, 
    		double b0, double b1, 
    	ArrayList<Double> bodies, ArrayList<Double> brains, int n) {
    	ArrayList<Double> vals = new ArrayList<>();

		double tb0 = b0;
		double tb1 = b1;
		double MSEval1 = 0;
		double MSEval2 = 0;
    	
		if (n != -1) {
			MSEval1 = partialMSEvalue2(b0, b1, bodies, brains, n);
			MSEval2 = partialMSEvalue(b0, b1, bodies, brains, n);
		}
		else {
			MSEval1 = partialMSEvalue2(b0, b1, bodies, brains, -1);
			MSEval2 = partialMSEvalue(b0, b1, bodies, brains, -1);
		}
				
		double b0t = tb0 - (eta * MSEval1);
		double b1t = tb1 - (eta * MSEval2);
		
		vals.add(b0t);
		vals.add(b1t);
		vals.add(MSEvalues(b0t, b1t, bodies, brains));
    	
    	return vals;
    }
    
   
    
    /**
     * Runs data analysis based on the given flag
     * and arguments. 
     * 
     * @param args: Flag
     */
    static public void main(String[] args){
    	List<List<String>> dataset = read();
    	int Flag = Integer.valueOf(args[0]);
    	
    	double brain_sum = 0;    
    	double body_sum = 0;
    	int count = dataset.size() - 1;
    	
    	ArrayList<Double> brain_list = new ArrayList<>();
		ArrayList<Double> body_list = new ArrayList<>();
		
		for(int i = 0; i < dataset.size(); i++) {
			if (i==0) {
				continue;
			}
			body_sum += Double.parseDouble(dataset.get(i).get(0));
			body_list.add(Double.parseDouble(dataset.get(i).get(0)));
			
			brain_sum += Double.parseDouble(dataset.get(i).get(1));
			brain_list.add(Double.parseDouble(dataset.get(i).get(1)));
		}
		
	
		double meanbody = meanvalue(body_sum, count);
		double mean_brain = meanvalue(brain_sum, count);
		
		
		double bodyStdev = stdevvalue(meanbody, body_list);
		double brainStdev = stdevvalue(mean_brain, brain_list);
		
		// Used to format all printed decimals
		DecimalFormat temp2 = new DecimalFormat("0.0000");
    	
		
    	if (Flag == 100) {
    		
    		System.out.println(Flag);
    		System.out.println(temp2.format(meanbody) + " " + 
    				temp2.format(bodyStdev));
    		System.out.println(temp2.format(mean_brain) + " " + 
    				temp2.format(brainStdev));
    		
    	}
    	
    	
    	if (Flag == 200) {
    		double b1 = Double.valueOf(args[1]);
    		double b2 = Double.valueOf(args[2]);
    		
    		System.out.println(temp2.format(MSEvalues(b1, b2, body_list, 
    				brain_list)));
    	}
    	
    	
    	if (Flag == 300) {
    		double b1 = Double.valueOf(args[1]);
    		double b2 = Double.valueOf(args[2]);
    		
    		System.out.println(temp2.format(partialMSEvalue2(b1, b2, body_list,
    				brain_list, -1)));
    		System.out.println(temp2.format(partialMSEvalue(b1, b2, body_list, 
    				brain_list, -1)));
    	}
    	
    	
    	if (Flag == 400) {
    		double eta = Double.valueOf(args[1]);
    		int t = Integer.valueOf(args[2]);
    		ArrayList<Double> temp = new ArrayList<>();
    		
    		for (int i = 1; i <= t; i++) {
    			if (i == 1) {
    				temp = gradDescent(i, eta, 0.0, 0.0, body_list, brain_list, -1);
    			}
    			else {
    				temp = gradDescent(i, eta, temp.get(0), temp.get(1), 
    						body_list, brain_list, -1);
    			}
    			System.out.println(i + " " + (temp2.format(temp.get(0))) + 
    					" " + temp2.format(temp.get(1)) + " " + 
    					temp2.format(temp.get(2)));
    		}
    	}
    	
    	
    	if (Flag == 500) {
    		
    		ArrayList<Double> values = Ordvalue(meanbody, mean_brain, body_list,
    				brain_list);
    		
    		for (double value : values) {
    			System.out.print(temp2.format(value) + " ");
    		}
    	}
    	
    	
    	if (Flag == 600) {
    		double predict = Double.valueOf(args[1]);
    		
    		ArrayList<Double> values = Ordvalue(meanbody, mean_brain, body_list,
    				brain_list);
    		
    		double value = values.get(0) + (values.get(1) * predict);
    		
    		System.out.println(temp2.format(value));
    	}
    	
    	
    	if (Flag == 700) {
    		double eta = Double.valueOf(args[1]);
    		int t = Integer.valueOf(args[2]);
    		ArrayList<Double> normalized = new ArrayList<>();
    		
    		for (double value : body_list) {
    			normalized.add(normalizevalue(value, meanbody, bodyStdev));
    		}
    		
    		ArrayList<Double> temp = new ArrayList<>();
    		
    		for (int i = 1; i <= t; i++) {
    			if (i == 1) {
    				temp = gradDescent(i, eta, 0.0, 0.0, normalized, 
    						brain_list, -1);
    			}
    			else {
    				temp = gradDescent(i, eta, temp.get(0), temp.get(1), 
    						normalized, brain_list, -1);
    			}
    			System.out.println(i + " " + (temp2.format(temp.get(0))) + " "
    					+ temp2.format(temp.get(1)) + " " 
    					+ temp2.format(temp.get(2)));
    		}
    	}
    	
    	
    	if (Flag == 800) {
    		double eta = Double.valueOf(args[1]);
    		int t = Integer.valueOf(args[2]);
    		ArrayList<Double> normalized = new ArrayList<>();
			
    		Random rand = new Random();
    		int n = rand.nextInt(body_list.size() + 1); 
    		
    		for (double value : body_list) {
    			normalized.add(normalizevalue(value, meanbody, bodyStdev));
    		}

    		
    		ArrayList<Double> temp = new ArrayList<>();
    		
    		for (int i = 1; i <= t; i++) {
    			if (i == 1) {
    				temp = gradDescent(i, eta, 0.0, 0.0, normalized,
    						brain_list, n);
    			}
    			else {
    				temp = gradDescent(i, eta, temp.get(0), temp.get(1),
    						normalized, brain_list, n);
    			}
    			System.out.println(i + " " + (temp2.format(temp.get(0))) + " " + 
    					temp2.format(temp.get(1)) + " " +
    					temp2.format(temp.get(2)));
    		}
    	}
    }

}
