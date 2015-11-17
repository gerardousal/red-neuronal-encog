package nd.nn.stdper.impl.service;

import nd.nn.stdper.impl.domain.MetaTabularData;
import nd.nn.stdper.impl.domain.MetaTabularDataCell;
import nd.nn.stdper.impl.domain.TabularData;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.plugin.system.SystemActivationPlugin;
import org.encog.util.arrayutil.NormalizationAction;
import org.encog.util.arrayutil.NormalizedField;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by driucorado on 11/11/15.
 */
public class TabularDataFactory {
    private CSVFormat csvFormat;
    private TabularData tabularData;

    public TabularDataFactory analizeFile(File file, String delimiter, String quote) throws IOException {
        FileReader reader = new FileReader(file);
        try {
            this.csvFormat = CSVFormat.DEFAULT.newFormat(delimiter.charAt(0)).withHeader().withQuote(quote.charAt(0));
            System.out.println(" delimiter:" + delimiter + " quote:" + quote + " file:" + file);
            this.tabularData = new TabularData();
            this.tabularData.file = file.getName();
            final CSVParser csvParser = new CSVParser(reader, csvFormat);
            this.tabularData.metaTabularData.header = csvParser.getHeaderMap();
            readFile(csvParser.iterator(), this.tabularData);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            throw new Error("Error duplicate header", ex);
        } finally {
            reader.close();
        }
        return this;
    }

    public TabularData getTabularData() {
        return tabularData;
    }

    private void readFile(Iterator<CSVRecord> iterator, TabularData tabularData) {
        List<List<Double>> rawData = new ArrayList<>();
        MetaTabularData metaTabularData = tabularData.metaTabularData;
        List<List<String>> dataTotal = new ArrayList<>();
        while (iterator.hasNext()) {
            CSVRecord next = iterator.next();
            List<Double> data = new ArrayList<>();
            List<String> row = new ArrayList<>();
            for (String field : tabularData.metaTabularData.header.keySet()) {
                double value = Double.parseDouble(next.get(field));
                if (!metaTabularData.metaCells.containsKey(field)) {
                    metaTabularData.metaCells.put(field, new MetaTabularDataCell(0, Double.MAX_VALUE));
                }
                if (value > metaTabularData.metaCells.get(field).max) {
                    metaTabularData.metaCells.get(field).max = value;
                } else if (value <= metaTabularData.metaCells.get(field).min) {
                    metaTabularData.metaCells.get(field).min = value;
                }
                metaTabularData.metaCells.get(field).addValue(value);
                data.add(value);
                row.add(value + "");
            }
            dataTotal.add(row);
            tabularData.data = dataTotal;
            rawData.add(data);
        }
        metaTabularData.rawData = rawData;
    }

}
