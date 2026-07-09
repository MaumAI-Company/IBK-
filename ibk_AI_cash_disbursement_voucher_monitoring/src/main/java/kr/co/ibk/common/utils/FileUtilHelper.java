package kr.co.ibk.common.utils;

import kr.co.ibk.domain.web.FileInfo;
import kr.co.ibk.model.FileInfoForm;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class FileUtilHelper {

    public static final int BUFF_SIZE = 2048;

    private static Logger log = LoggerFactory.getLogger(FileUtilHelper.class);

    private static String filepath;

    @Value("${Globals.fileStorePath}")
    private void setValue(String value){
        filepath = value;
    }

    private static String filepathURI;
    @Value("${Globals.fileStoreUriPath}")
    private void setValues(String values){
        filepathURI = values;
    }


    /**
     * 첨부파일을 서버에 저장한다.
     *
     * @param file
     * @param stordFilePath
     * @throws Exception
     */
    public static FileInfo writeUploadedFile(MultipartFile file, String stordFilePath, FileInfoForm fileForm) throws Exception {
        if (file == null) {
            return null;
        }

        InputStream stream = null;
        OutputStream bos = null;

        String fileSavePath = stordFilePath;
        if ( "".equals(fileSavePath) ) {
            fileSavePath = filepath;
        } else {
            fileSavePath = filepath + "/" + fileSavePath;
        }

        try {
            stream = file.getInputStream();
            File dir = new File(fileSavePath);
            if (!dir.isDirectory()) {
                boolean _flag = dir.mkdir();
                if (!_flag) {
                    throw new IOException("Directory creation Failed ");
                }
            }

            String sourceFileName = file.getOriginalFilename();
            if (sourceFileName != null) {
                String temp[] = sourceFileName.split("\\\\");
                if (temp != null && temp.length > 0) {
                    sourceFileName = temp[temp.length - 1];
                }
            }

            String newfilename = sourceFileName;  //폴더정보를 제외한 파일명.
            String sourceFileNameExtension = FileUtilHelper.getFileExtensionToLower(sourceFileName);  //확장자명.


            //서버에 파일 중복확인
            File cFile2 = new File(fileSavePath + "/" + newfilename);

            if ( cFile2.exists() ) {

                String filenameWithoutExt = getFilenameWithoutExtension(sourceFileName); // 확장자를 제외한 파일명.

                long tmpSeq = System.currentTimeMillis();  //중복회피를 위한 임시번호

                boolean bexist = true;

                do {
                    //신규파일명 : 기존파일명 + "_seq." + 기존확장자
                    newfilename = filenameWithoutExt + "_" + tmpSeq + "." + sourceFileNameExtension;
                    cFile2 = new File(fileSavePath + "/" + newfilename);
                    if ( cFile2.exists() ) {
                        tmpSeq ++;
                    } else {
                        bexist = false;
                    }

                } while ( bexist );
            }



            //OutputStream bos = null;
            String fullPath = fileSavePath + "/" + newfilename;
            bos = new FileOutputStream(fullPath);

            int bytesRead = 0;
            byte[] buffer = new byte[BUFF_SIZE];

            while ((bytesRead = stream.read(buffer, 0, BUFF_SIZE)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            log.debug("==서버에파일이생성되었습니다.경로:{}, 원본파일명:{}", fullPath, file.getOriginalFilename());

            FileInfo fileInfo = new FileInfo();
            fileInfo.setFlNm(sourceFileName);
            fileInfo.setChgFlNm(newfilename);
            fileInfo.setFlSz(file.getSize());
            fileInfo.setFlExtsn(sourceFileNameExtension);
            fileInfo.setFlPth(filepathURI + "/" + stordFilePath);
            fileInfo.setRegDtHms(LocalDateTime.now());
            fileInfo.setDataPid(fileForm.getDataPid());
            fileInfo.setTableNm(fileForm.getTableNm());
            if (!NullHelper.isNull(fileForm.getDvTy())){
                fileInfo.setDvTy(fileForm.getDvTy());
            }

            //return fileService.makeNer(fileDto);

            return fileInfo;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception ignore) {
                    ignore.printStackTrace();
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception ignore) {
                    ignore.printStackTrace();
                }
            }
        }
    }

    /**
     * 서버의 파일을 다운로드한다.
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public static void download(HttpServletRequest request, HttpServletResponse response, String storedFilePath) throws Exception {
        String downFileName = "";
        //String orgFileName = "";

        if (request.getAttribute("downFile") == null) {
            downFileName = "";
        } else {
            if (storedFilePath == null || "".equals(storedFilePath)) {
                downFileName = filepath + request.getAttribute("downFile");
            } else {
                downFileName = filepath + "/" + storedFilePath + "/" + request.getAttribute("downFile");
            }
        }
        File downloadFile = new File(downFileName);
        if (!downloadFile.exists()) {
            throw new FileNotFoundException(downFileName);
        }

        if (!downloadFile.isFile()) {
            throw new FileNotFoundException(downFileName);
        }

        byte fileByte[] = readFileToByteArray(downloadFile);
        response.setContentType("application/octet-stream");
        response.setContentLength(fileByte.length);

        String userAgent = request.getHeader("User-Agent");
        boolean ie = (userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("Trident") > -1 || userAgent.indexOf("Edge") > -1);
        String fileName = "";
        if (ie) {
            fileName = URLEncoder.encode(downloadFile.getName(), "UTF-8");
            fileName = fileName.replaceAll("\\+", "%20");
        } else {
            fileName = new String(String.valueOf(downloadFile.getName()).getBytes("UTF-8"), "iso-8859-1");
        }

        response.setHeader("Content-Disposition", "attachment; fileName=\"" +fileName+"\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.getOutputStream().write(fileByte);

        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

    private static String getFileExtension(File file) {
        return getFileExtension(file.getAbsolutePath());
    }


    /**
     * 파일명에서 확장자만 리턴함. (abc/file1.txt --> txt)
     * @param filename
     * @return
     */
    public static String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty() ) return "";

        String fileExtension = StringUtils.getFilenameExtension(filename);

        return fileExtension;
    }


    /**
     * 파일의 확장자명을 리턴함.
     * @param filename
     * @return
     */
    public static String getFileExtensionToLower(String filename) {

        String fileExtension = getFileExtension(filename);

        return fileExtension.toLowerCase();
    }

    /**
     * 서버의 파일을 삭제처리함.
     * @param stordFilePath
     * @param fileNm
     * @return
     */
    public static boolean removeFile(String stordFilePath, String fileNm) {
        boolean rtnBool = false;
        String path = filepath + File.separator + stordFilePath + File.separator + fileNm;
        File file = new File(path);
        if (file.exists()) {
            rtnBool = file.delete();
            log.debug("서버파일삭제결과:{}, 파일:{}", rtnBool, file.getAbsoluteFile());
        }

        return rtnBool;
    }

    /**
     * 서버의 폴더를 삭제처리함.
     */
    public static boolean removeDirectory(String stordFilePath, String directoryNm) {
        boolean rtnBool = false;
        String path = filepath + File.separator + stordFilePath + File.separator + directoryNm;
        File folder = new File(path);
        try {
            if (folder.exists()) {
                FileUtils.cleanDirectory(folder);
                if (folder.isDirectory()) {
                    folder.delete();
                }
            }
            rtnBool = true;
        } catch (IOException e) {
           return false;
        }
        return rtnBool;
    }

    public static byte[] readFileToByteArray(File file) throws IOException {
        InputStream in = null;
        try {
            in = openInputStream(file);
            return IOUtils.toByteArray(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
    public static FileInputStream openInputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (file.canRead() == false) {
                throw new IOException("File '" + file + "' cannot be read");
            }
        } else {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        }
        return new FileInputStream(file);
    }


    /**
     * 서버에 저장된 물리적 파일 전체경로를 리턴함.
     * @param fileInfo
     * @param subDirectory
     * @return
     */
    public static String serverFullFilePath(FileInfo fileInfo, String subDirectory) {
        String file = "";
        String dirSlash = "/";
        //file = filepath + File.separator + subDirectory + File.separator + attachFile.getChgFlNm();   // File.separator 사용시 오류
        file = filepath + dirSlash + subDirectory + dirSlash + fileInfo.getChgFlNm();

        return file;
    }


    /**
     * text파일을 읽어 리턴함.  classpath 기준에 위치하는 파일 , 경로를 지정시에선 config/abc.txt 형식으로
     * @param filepath
     * @return
     * @throws IOException
     */
    public static List<String> readLines(String filepath) throws IOException {

        File file = null;

        //클래스패스에서 먼저 검색후 파일이 없을 경우 실제경로에 파일찾기.
        try {
            file = ResourceUtils.getFile("classpath:" + filepath);
        } catch (FileNotFoundException e) {
            log.debug("classpath에서 찾기실패. 실제경로에서 찾기:{}", filepath);
            file = ResourceUtils.getFile(filepath);
        }

        log.debug("실제경로:{}", file.getAbsolutePath());
        //Read File Content
        //String content = new String(Files.readAllBytes(file.toPath()));
        //System.out.println(content);

        FileReader filereader = new FileReader(file);


        //입력버퍼생성
        BufferedReader bufReader = new BufferedReader(filereader);

        List<String> texts = new ArrayList<String>();
        String line = "";
        while ( (line = bufReader.readLine()) != null ) {
            texts.add(line);
        }
        bufReader.close();


        log.debug("파일읽기완료. filename:{}, 총줄수:{}", filepath, texts.size());
        return texts;
    }



    /**
     * 문자열경로에서 파일 객체를 리턴함. 없을경우 empty.
     * @param stringFilePath
     * @return
     */
    public static Optional<File> getFileByStringPath(String stringFilePath) {
        if ( stringFilePath == null || stringFilePath.isEmpty() ) return Optional.empty();


        File f = new File(stringFilePath);
        if ( f.exists() == false ) {
            log.info("파일없음.:{}", stringFilePath);
            return Optional.empty();
        }

        return Optional.of(f);
    }


    /**
     * 엑셀파일 여부 리턴. 확장자명으로만 체크.
     * @param file
     * @return
     */
    public static boolean isExcelFile(File file) {
        if ( file == null ) return false;

        String ext = getFileExtension(file).toLowerCase().trim();

        List<String> excelExt = new ArrayList<String>();

        excelExt.add("xls");
        excelExt.add("xlsx");

        return excelExt.contains(ext);
    }


    /**
     * 파일명에서 폴더경로, 확장자, .을  제외한 파일명만 리턴함.
     * @param fullFilePath
     * @return
     */
    public static String getFilenameWithoutExtension(String fullFilePath) {

        if ( fullFilePath == null ) return "";

        String filename = StringUtils.getFilename(fullFilePath);

        String filenameWithoutExt = StringUtils.stripFilenameExtension(filename);

        return filenameWithoutExt;

    }

}
