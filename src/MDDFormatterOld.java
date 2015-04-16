/**
 * Created by edward_salcido on 2/18/15.
 */

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.io.File;

//import FileChooser;

import javax.swing.*;


public class MDDFormatterOld {

    public static final String SURVEY_TYPE = "Survey";
    public static final String CATEGORY_TYPE = "Category";

    static String surveyName = "SurveyNameSurvey";
    static String categoryName = "Category1";

    public static void main(String[] args) throws IOException {

        String directory = "/Users/edward_salcido/Documents/Surveys/First Niagara/mdd conversion/";
        String inputfilePath = directory + "FirstNiagara-pre-MDM.xlsx";
        String outputFileName = directory + "FirstNiagara-MDM-withanswertab.csv";
        String outputSheet = "FirstNiagara-pre-MDM.csv";

       //file stuff
       // String file = getFile();
        //System.out.println("file: "+file);

        FileInputStream fs = new FileInputStream(inputfilePath);
        Workbook workbook = new XSSFWorkbook(fs);
        //main tab we are working with from input file
        Sheet sheet = workbook.getSheet(outputSheet);

        FileOutputStream fileOut = new FileOutputStream(outputFileName);
        HSSFWorkbook fileoutwb = new HSSFWorkbook();


        //create 3 tabs for MDM Structure file
        HSSFSheet structureTab = fileoutwb.createSheet("Structure");
        HSSFSheet answerTab = fileoutwb.createSheet("Answer");
        HSSFSheet metadataTab = fileoutwb.createSheet("Metadata");



        Iterator<Row> rowIterator = sheet.rowIterator();
        List<String> answerList = new ArrayList<String>();

        //get sheet from outfile
        HSSFSheet outsheetstructure = fileoutwb.getSheetAt(0);
        HSSFSheet outsheetanswer = fileoutwb.getSheetAt(1);
        HSSFSheet outsheetmetadata = fileoutwb.getSheetAt(2);

        createTopPortion(outsheetstructure);
        createAnswerTop(outsheetanswer);
        createMetadataTop(outsheetmetadata);

        //create hierarchical column
        //starting row from outfile
        int f=3;
        String hierarchy = "1.1.";
        int hierarchyCount=1;
        //get column
        rowIterator.next();
        while ( rowIterator.hasNext() ){
            try {
            Row currentRow = rowIterator.next();
            currentRow.getHeight();
            if (currentRow.getCell(0) != null) {

                //get from row
                Hashtable<String,String> row1 = getRow(currentRow);

//                    //get contents from input file
//                    String variableID = "";//currentRow.getCell(0).getStringCellValue();
//                    String shortName = "";//currentRow.getCell(4).getStringCellValue();
//                    String longName = "";//currentRow.getCell(4).getStringCellValue();
//                    String varType= "";//currentRow.getCell(9).getStringCellValue();
//                    String respType ="";// currentRow.getCell(10).getStringCellValue();


                //get contents from input file
                String variableID = currentRow.getCell(0).getStringCellValue();
                String shortName = currentRow.getCell(4).getStringCellValue();
                String longName = currentRow.getCell(4).getStringCellValue();
                String varType= currentRow.getCell(9).getStringCellValue();
                String respType =currentRow.getCell(10).getStringCellValue();


                //if variable type from input file is string, convert to Reference Data
                    if(varType.equals("string")){
                        varType = "Reference Data";
                    }



                    //filter out the vartypes to print the correct spelling of the response data type.
                    if(respType.equals("single")){
                        respType = "Single Selection";
                    }else if(respType.equals("multi")){
                        respType = "Multi Selection";
                    }else if(respType.equals("userInput")){
                        respType = "User Input";
                    }
                    else{}

//                if(row1.get("vartype").equals("string")){
//                    varType = "Reference Data";
//                }
//                if(row1.get("resptype").equals("single")){
//                    respType = "Single Selection";
//                }else if(row1.get("resptype").equals("multi")){
//                    respType = "Multi Selection";
//                }else if(row1.get("resptype").equals("userInput")){
//                    respType = "User Input";
//                }
//                else{}


                //debugging
                    answerList.add(variableID);

                    //create row
                    Row row = outsheetstructure.createRow(f);

                    //hierarchical rank row
                    Cell r1ac1a = row.createCell(0);
                    r1ac1a.setCellValue(hierarchy+hierarchyCount);

                    //Variable ID row
                    Cell r1ac2a = row.createCell(1);
                    r1ac2a.setCellValue(variableID);

                    //Variable Short Name
                    Cell r1ac3a = row.createCell(2);
                    r1ac3a.setCellValue(shortName);

                    //Variable long Name
                    Cell r1ac4a = row.createCell(3);
                    r1ac4a.setCellValue(longName);

                    //Variable Type
                    Cell r1ac5a = row.createCell(4);
                    r1ac5a.setCellValue(varType);

                    //Response Type
                    Cell r1ac6a = row.createCell(5);
                    r1ac6a.setCellValue(respType);

                    //response data type
                    Cell r1ac7a = row.createCell(6);
                    r1ac7a.setCellValue("Text");

                    //Column Mapping
                    Cell r1ac8a = row.createCell(7);
                    r1ac8a.setCellValue(variableID);

                    //increment counts
                    f++;
                    hierarchyCount++;

            }
            }catch(Exception e){
            System.out.println("Error :"+e);
            }

            //start answer tab processing here


        }

        //Debugging print answer list
        for (String s : answerList) {
            System.out.println(s+" ");
        }

        //write to file
        fileoutwb.write(fileOut);
        fileOut.flush();
        fileOut.close();


    }//end main

    public static Hashtable getRow(Row currentRow){
        Hashtable<String,String> row1 = new Hashtable<String,String>();

        row1.put("variableId",currentRow.getCell(0).getStringCellValue());
        row1.put("shortname",currentRow.getCell(4).getStringCellValue());
        row1.put("longmame",currentRow.getCell(4).getStringCellValue());
        row1.put("vartype",currentRow.getCell(9).getStringCellValue());
        row1.put("resptype",currentRow.getCell(10).getStringCellValue());

        return row1;
    }


    //get file from user
    public static String getFile(){

        String dataMapFile1 = "";
        //get the file for the data map
        Component frame=null;
        //prompt user to choose a file
        JOptionPane.showMessageDialog(frame, "Please choose Data Map");

        File b;
        //open file chooser
        JFileChooser fileChooser = new JFileChooser();

        int a = fileChooser.showOpenDialog(null);

        if(a == JFileChooser.APPROVE_OPTION){
            b = fileChooser.getSelectedFile();

            System.out.println(b.getAbsolutePath() );
            dataMapFile1 = b.getAbsolutePath();
        }
        return dataMapFile1;
    }

    public static void createTopPortion( HSSFSheet outsheet){

        //create top portion
        ArrayList<String> topHeader = new ArrayList<String>();
        ArrayList<String> surveyRow = new ArrayList<String>();
        ArrayList<String> categoryRow = new ArrayList<String>();

        topHeader.add("Hierarchical Rank");
        topHeader.add("Variable ID");
        topHeader.add("Variable Short Name");
        topHeader.add("Variable Long Name");
        topHeader.add("Variable Type");
        topHeader.add("Response Type");
        topHeader.add("Response Data Type");
        topHeader.add("Column Mapping");
        topHeader.add("Inactive");
        topHeader.add("Custom Function");

        surveyRow.add("1");
        surveyRow.add(surveyName);
        surveyRow.add(surveyName);
        surveyRow.add(surveyName);
        surveyRow.add(SURVEY_TYPE);

        categoryRow.add("1.1");
        categoryRow.add(categoryName);
        categoryRow.add(categoryName);
        categoryRow.add(categoryName);
        categoryRow.add(CATEGORY_TYPE);
        categoryRow.add("N/A");
        categoryRow.add("N/A");
        categoryRow.add("N/A");

       //create top row
        write2xl(outsheet, 0,topHeader);

        //create survey row
        write2xl(outsheet, 1, surveyRow);

        //create category row
        write2xl(outsheet,2,categoryRow);

    }

    public static void createAnswerTop(HSSFSheet outsheet){

        ArrayList<String> topheader = new ArrayList<String>();

        topheader.add("Variable ID");
        topheader.add("Response Code");
        topheader.add("Response Label");
        topheader.add("Response Mapping Column");

        write2xl(outsheet, 0,topheader);

    }

    public static void createMetadataTop(HSSFSheet outsheet){

        ArrayList<String> topheader = new ArrayList<String>();

        topheader.add("Variable ID");
        topheader.add("Metadata Type");
        topheader.add("Response Type");
        topheader.add("Response Data Type");
        topheader.add("Mapping Column");

        write2xl(outsheet, 0,topheader);

    }

    public static void write2xl(HSSFSheet outsheet,int rowNum, ArrayList<String> parentRow){

        int num = rowNum;

        HSSFRow row = outsheet.createRow(rowNum);
        for(int i=0;i<parentRow.size();i++){
            HSSFCell rc = row.createCell(i);
            rc.setCellValue(parentRow.get(i));
        }

    }


}//end class
