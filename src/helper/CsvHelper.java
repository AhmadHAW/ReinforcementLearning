package helper;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.*;
import org.supercsv.prefs.CsvPreference;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvHelper {

    private static final String CSV_FILENAME = "src/helper/examples/sample";

    public static void main(String[] args) throws Exception {
        readWithCsvListReader();
        //readWithCsvMapReader();
        //writeWithCsvListWriter();
        writeWithCsvMapWriter();
    }

    /**
     * Sets up the processors used for the examples. There are 10 CSV columns, so 10 processors are defined. Empty
     * columns are read as null (hence the NotNull() for mandatory columns).
     *
     * @return the cell processors
     */
    private static CellProcessor[] getProcessors() {

        final CellProcessor[] processors = new CellProcessor[] {
                new Optional(), // constructor_parameter
                new Optional(), // avg_runtime
                new Optional(), // avg_reward
                new Optional(), // best_reward
                new Optional(), // text
        };
        return processors;
    }

    private static String[] getHeader() {
        return new String[] { "constructor_parameter", "avg_runtime", "avg_reward", "best_reward", "text" };
    }

    /**
     * An example of reading using CsvListReader.
     */
    private static void readWithCsvListReader() throws Exception {

        ICsvListReader listReader = null;
        try {
            listReader = new CsvListReader(new FileReader(CSV_FILENAME + ".csv"), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

            listReader.getHeader(true); // skip the header (can't be used with CsvListReader)
            final CellProcessor[] processors = getProcessors();

            List<Object> customerList;
            while( (customerList = listReader.read(processors)) != null ) {
                System.out.println(String.format("lineNo=%s, rowNo=%s, ListContent=%s", listReader.getLineNumber(),
                        listReader.getRowNumber(), customerList));
            }

        }
        finally {
            if( listReader != null ) {
                listReader.close();
            }
        }
    }

    /**
     * An example of reading using CsvMapReader.
     */
    private static void readWithCsvMapReader() throws Exception {

        ICsvMapReader mapReader = null;
        try {
            mapReader = new CsvMapReader(new FileReader(CSV_FILENAME + ".csv"), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

            // the header columns are used as the keys to the Map
            final String[] header = mapReader.getHeader(true);
            final CellProcessor[] processors = getProcessors();

            Map<String, Object> customerMap;
            while( (customerMap = mapReader.read(header, processors)) != null ) {
                System.out.println(String.format("lineNo=%s, rowNo=%s, MapContent=%s", mapReader.getLineNumber(),
                        mapReader.getRowNumber(), customerMap));
            }

        }
        finally {
            if( mapReader != null ) {
                mapReader.close();
            }
        }
    }

    /**
     * An example of reading using CsvListWriter.
     */
    private static void writeWithCsvListWriter() throws Exception {

        // create the customer Lists (CsvListWriter also accepts arrays!)
        final List<Object> firstRow = Arrays.asList(new Object[] { "arg1,arg2,arg3", 367, -4.2, 2.2, "q"});
        final List<Object> secondRow = Arrays.asList(new Object[] { "arg1,arg2,arg3", 4141, 1.33, 8.65, "s"});

        ICsvListWriter listWriter = null;
        try {
            listWriter = new CsvListWriter(new FileWriter(CSV_FILENAME + "_list_out.csv"),
                    CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

            final CellProcessor[] processors = getProcessors();
            final String[] header = getHeader();

            // write the header
            listWriter.writeHeader(header);

            // write the customer lists
            listWriter.write(firstRow, processors);
            listWriter.write(secondRow, processors);

        }
        finally {
            if ( listWriter != null ) {
                listWriter.close();
            }
        }
    }

    /**
     * An example of reading using CsvMapWriter.
     */
    private static void writeWithCsvMapWriter() throws Exception {

        final String[] header = getHeader();

        // create the customer Maps (using the header elements for the column keys)
        final Map<String, Object> firstRow = new HashMap<>();
        firstRow.put(header[0], "arg1,arg2,arg3");
        firstRow.put(header[1], 367);
        firstRow.put(header[2], -4.2);
        firstRow.put(header[3], 2.2);
        firstRow.put(header[4], "q");

        final Map<String, Object> secondRow = new HashMap<>();
        secondRow.put(header[0], "arg1,arg2,arg3");
        secondRow.put(header[1], 4141);
        secondRow.put(header[2], 1.33);
        secondRow.put(header[3], 8.65);
        secondRow.put(header[4], "s");

        ICsvMapWriter mapWriter = null;
        try {
            mapWriter = new CsvMapWriter(new FileWriter(CSV_FILENAME + "_map_out.csv"),
                    CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

            final CellProcessor[] processors = getProcessors();

            // write the header
            mapWriter.writeHeader(header);

            // write the customer maps
            mapWriter.write(firstRow, header, processors);
            mapWriter.write(secondRow, header, processors);

        }
        finally {
            if( mapWriter != null ) {
                mapWriter.close();
            }
        }
    }
}
