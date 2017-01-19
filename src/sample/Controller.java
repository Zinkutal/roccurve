package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Controller
{
    private Hashtable<double[], Boolean> train = new Hashtable<>();
    private Hashtable<double[], Boolean> test = new Hashtable<>();

    private static final int _trainCount = 20;
    private static final int _classElementCount = 50;
    private static final int _iterationCount = 10000;
    private static final double _alpha = 0.01;

    private static final int TARGET_CLASS = 1; // [1;3]

    private double[] thetas = new double[4];

    private void ReadData()
    {
        if(TARGET_CLASS > 3 || TARGET_CLASS < 1) throw new IllegalArgumentException("TARGET_CLASS must be more than 0 and less than 4 !!!!1!11");

        String csvFile = "iris.csv";
        String line = "";
        String cvsSplitBy = ",";
        boolean isFirstClass = true;
        int currentClass = 1;
        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile)))
        {
            double[] features = new double[4];
            while ((line = br.readLine()) != null)
            {
                String[] elements = line.split(cvsSplitBy);
                for (int i =0; i <4 ; i++)
                {
                    features[i] = Double.valueOf(elements[i]);
                }

                if ((count < _trainCount))
                {
                    train.put(features.clone(), currentClass == TARGET_CLASS);
                } else
                    {
                        test.put(features.clone(), currentClass == TARGET_CLASS);
                    }

                count++;

                if ((count == _classElementCount))
                {
                    count = 0;
                    isFirstClass = false;
                    currentClass++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double LogisticCurve(double z) {
        return (1 / (1 + Math.exp((z * -1))));
    }

    private double ScalarMultiply(double[] v1, double[] v2) {
        double res = 0;
        for (int i = 0; (i < v1.length); i++)
        {
            res = (res + (v1[i] * v2[i]));
        }
        return res;
    }

    private void TrainRegression() {
        for (int i = 0; (i < _iterationCount); i++) {
            double[] currThetas;
            currThetas = Arrays.copyOf(this.thetas, this.thetas.length);
            for (int j = 0; (j < 4); j++) {
                double sum = 0;
                for (double[] doubles : this.train.keySet()) {
                    boolean iris = this.train.get(doubles);
                    double rightAns = iris ? 1 : 0;
                    sum = (sum + ((rightAns - this.LogisticCurve(this.ScalarMultiply(currThetas, doubles))) * doubles[j]));
                }

                sum /= this.train.size();
                sum *= _alpha;
                this.thetas[j] = currThetas[j] + sum;
            }

        }

    }

    public final LinkedList<javafx.util.Pair<Double, Double>> Classify() {
        this.ReadData();
        this.TrainRegression();
        LinkedList<javafx.util.Pair<Double, Double>> result = new LinkedList<>();

        for (double threshhold = 0.0; (threshhold <= 1.0); threshhold += 0.001) {

            double TPR = 0;
            double FPR = 0;

            Enumeration<double[]> enumeration = this.test.keys();
            while (enumeration.hasMoreElements()) {
                double[] nextEl = enumeration.nextElement();
                double currAns = LogisticCurve(ScalarMultiply(this.thetas, nextEl));

                if ((currAns >= threshhold)) {
                    if (test.get(nextEl)){
                        TPR++;
                    } else {
                        FPR++;
                    }
                }
            }

            double p = (_classElementCount - _trainCount);
            double n = (p * 2);

            TPR /= p;
            FPR /= n;
            //Point point = new Point(1,1);
            result.addLast(new javafx.util.Pair<>(FPR, TPR));

            //System.out.println("fpr = " + FPR + "; tpr = " + TPR);
        }

        return result;
    }
}