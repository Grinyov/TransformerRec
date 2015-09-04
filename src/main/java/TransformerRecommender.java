
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.apache.commons.cli2.OptionException;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

/**
 * Created by green on 04.09.2015.
 *
 *
 *  ласс преобразовывает файл ratings.dat из формата GroupLens в формате CSV
 * создает класс модели, который будет обрабатывать рейтинги
    из CSV-файл и создает простой –екомендатель на этой модели.
 » извлекает рекомендации содержащиес€ в данных ratings.csv

 */
public class TransformerRecommender {

    static final String inputFile = "d:\\develop\\ml-1m\\ratings.dat";
    static final String outputFile = "d:\\develop\\ml-1m\\ratings.csv";


    // transformed ratings.dat from GroupLens to CSV
    private static void CreateCsvRatingsFile() throws FileNotFoundException, IOException
    {
        BufferedReader br = new BufferedReader(new
                FileReader(inputFile));
        BufferedWriter bw = new BufferedWriter(new
                FileWriter(outputFile));
        String line = null;
        String line2write = null;
        String[] temp;
        int i = 0;
        while (
                (line = br.readLine()) != null
                        &&
                        i < 10000
                )
        {
            i++;
            temp = line.split("::");
            line2write = temp[0] + "," + temp[1];
            bw.write(line2write);
            bw.newLine();
            bw.flush();
        }
        br.close();
        bw.close();
    }

    public static void main( String[] args ) throws IOException, TasteException, OptionException {

        CreateCsvRatingsFile();

        // create data source (model) - from the csv file

        File ratingsFile = new File(outputFile);
        DataModel model = new FileDataModel(ratingsFile);

        // create a simple recommender on our data
        CachingRecommender cachingRecommender = new
                CachingRecommender(new SlopeOneRecommender(model));
// for all users
        for (LongPrimitiveIterator it = model.getUserIDs();
             it.hasNext();)
        {
            long userId = it.nextLong();
            // get the recommendations for the user
            List<RecommendedItem> recommendations = cachingRecommender.
                    recommend(userId, 10);
// if empty write something
            if (recommendations.size() == 0){
                System.out.print("User ");
                System.out.print(userId);
                System.out.println(": no recommendations");
            }
// print the list of recommendations for each
            for (RecommendedItem recommendedItem : recommendations) {
                System.out.print("User ");
                System.out.print(userId);
                System.out.print(": ");
                System.out.println(recommendedItem);
            }
        }
    }
}
