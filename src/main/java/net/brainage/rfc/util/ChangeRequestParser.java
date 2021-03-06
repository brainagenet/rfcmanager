/*
 * net.brainage.rfc.util.ChangeRequestParser.java
 * Created on 2011. 6. 20.
 */
package net.brainage.rfc.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import jxl.Cell;
import jxl.JXLException;
import jxl.Sheet;
import jxl.Workbook;
import net.brainage.rfc.Constants;
import net.brainage.rfc.model.ChangeRequest;
import net.brainage.rfc.model.ChangeRequestResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public class ChangeRequestParser
{
    
    private static final Logger log = LoggerFactory.getLogger(ChangeRequestParser.class); 

    public static class ChangeRequestFormInfo
    {

        public static final int COLUMN_NO = 1;
        public static final int COLUMN_ID = 2;
        public static final int COLUMN_RESOURCE = 3;
        public static final int COLUMN_REVISION = 6;
        public static final int COLUMN_CRTYPE = 7;

        public static final int ROW_COMPONENT = 2;
        public static final int COLUMN_COMPONENT = 3;

        public static final int ROW_MODULE = 2;
        public static final int COLUMN_MODULE = 5;

        public static final int ROW_REQDATE = 2;
        public static final int COLUMN_REQDATE = 7;

        public static final int ROW_REQUESTER = 3;
        public static final int COLUMN_REQUESTER = 3;

        public static final int ROW_PRCDATE = 3;
        public static final int COLUMN_PRCDATE = 7;

        public static final int ROW_SUMMARY = 4;
        public static final int COLUMN_SUMMARY = 3;

    }

    /**
     * 
     */
    private ChangeRequest changeRequest;

    /**
     * @param changeRequest
     */
    public ChangeRequestParser(ChangeRequest changeRequest) {
        this.changeRequest = changeRequest;
    }

    /**
     * @param filename
     * @throws JXLException
     * @throws IOException
     */
    public void parse(String filename) throws JXLException, IOException {
        File f = new File(filename);
        parse(f);
    }

    /**
     * @param formFile
     * @throws JXLException
     * @throws IOException
     */
    public void parse(File formFile) throws JXLException, IOException {
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(new BufferedInputStream(new FileInputStream(formFile)));
            Sheet sheet = workbook.getSheet(0);
            changeRequest.initialize();
            loadInformation(sheet);
            loadResourceItems(sheet);
        } finally {
            if ( workbook != null ) {
                workbook.close();
                workbook = null;
            }
        }
    }

    /**
     * @param sheet
     */
    private void loadInformation(Sheet sheet) {
        // load component
        String component = getCellContent(sheet, ChangeRequestFormInfo.COLUMN_COMPONENT,
                ChangeRequestFormInfo.ROW_COMPONENT);
        for ( int i = 0, l = Constants.COMPONENT_LIST.length ; i < l ; i++ ) {
            String c = Constants.COMPONENT_LIST[i];
            if ( c.equals(component) ) {
                changeRequest.setComponentSelect(i);
                changeRequest.setComponent(c);
                break;
            }
        }

        // load module
        String module = getCellContent(sheet, ChangeRequestFormInfo.COLUMN_MODULE,
                ChangeRequestFormInfo.ROW_MODULE);
        for ( int i = 0, l = Constants.MODULE_LIST.length ; i < l ; i++ ) {
            String m = Constants.MODULE_LIST[i];
            if ( m.equals(module) ) {
                changeRequest.setModuleSelect(i);
                changeRequest.setModule(m);
                break;
            }
        }

        // load request date
        String reqDate = getCellContent(sheet, ChangeRequestFormInfo.COLUMN_REQDATE,
                ChangeRequestFormInfo.ROW_REQDATE);
        changeRequest.setRequestDate(reqDate);

        // load requester
        String requester = getCellContent(sheet, ChangeRequestFormInfo.COLUMN_REQUESTER,
                ChangeRequestFormInfo.ROW_REQUESTER);
        changeRequest.setRequester(requester);

        // load summary
        String summary = getCellContent(sheet, ChangeRequestFormInfo.COLUMN_SUMMARY,
                ChangeRequestFormInfo.ROW_SUMMARY);
        changeRequest.setSummary(summary);
    }

    /**
     * @param sheet
     */
    private void loadResourceItems(Sheet sheet) {
        int startRow = 6;
        int lastRow = sheet.getRows();

        for ( int row = startRow ; row < lastRow ; row++ ) {
            String resource = getCellContent(sheet, ChangeRequestFormInfo.COLUMN_RESOURCE, row);
            if ( resource == null || resource.length() == 0 ) {
                break;
            }
            ChangeRequestResource res = new ChangeRequestResource();
            String cellValue;
            res.setNo(Integer.parseInt(getCellContent(sheet, ChangeRequestFormInfo.COLUMN_NO, row)));
            res.setResource(resource);
            cellValue = getCellContent(sheet, ChangeRequestFormInfo.COLUMN_REVISION, row);
            if ( StringUtils.hasText(cellValue) ) {
                res.setRevision(Long.parseLong(cellValue));
            }

            cellValue = getCellContent(sheet, ChangeRequestFormInfo.COLUMN_CRTYPE, row);
            if ( StringUtils.hasText(cellValue) ) {
                for ( String type : Constants.STATUS_TEXTS ) {
                    if ( type.equals(cellValue.toLowerCase()) ) {
                        res.setType(type);
                        break;
                    }
                }
            }
            changeRequest.addResource(res);
        }
    }

    /**
     * @param sheet
     * @param column
     * @param row
     * @return
     */
    public static String getCellContent(Sheet sheet, int column, int row) {
        Cell c = sheet.getCell(column, row);
        return c.getContents();
    }

}
