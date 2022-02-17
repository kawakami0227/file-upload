package com.example.file_upload_demo.page;

import com.example.file_upload_demo.service.IPredictionService;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.github.openjson.JSONObject;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.lang.Bytes;
import org.wicketstuff.annotation.mount.MountPath;

@MountPath("Upload")
public class UploadPage extends WebPage{

    @SpringBean
    private IPredictionService predictionService;

    private FileUploadField uploadField;
    private File uploadDir;


    public UploadPage(){

        // Formを拡張したUpload用フォーム
        add(new UploadForm("uploadForm"));

        // アップロードディレクトリからファイルのリスト（配列）を取得
        File[] file = uploadDir.listFiles();

        // ダウンロード用リンクのリスト
        add(new DownloadListView("downloadList", Arrays.asList(file)));
    }

    private File getDirectory(String dirName){
        WebApplication application = (WebApplication)getApplication();
        // 絶対パスの取得　保存などの操作で相対パスは、Webアプリケーションでは危険
        String uploadPath = application.getServletContext().getRealPath(dirName);
        File dir = new File(uploadPath);

        if(!dir.exists())
            dir.mkdir();

        return dir;
    }

    private class DownloadListView extends ListView{
        public DownloadListView(String id, List list) {
            super(id, list);
        }

        @Override
        protected void populateItem(ListItem item) {
            final File file = (File)item.getModelObject();
            // ダウンロード用リンクの作成
            DownloadLink link = new DownloadLink("downloadLink", file){
                @Override
                public void onComponentTagBody(MarkupStream markupStream,
                                                  ComponentTag openTag) {
                    // アンカータグの中身（<a href="#">～</a>　の　～の部分）をファイルの名前で置き換える
                    replaceComponentTagBody(markupStream, openTag, file.getName());
                }
            };
            item.add(link);

            String url = "http://127.0.0.1:5000/predict/" + file.toString();
            url = url.replace("\\", "/");
            getResult getresult = new getResult();
            String pre = getresult.getResult(url);
            JSONObject jsonObject = new JSONObject(pre);
            pre = jsonObject.getString("prediction");
            var preModel = Model.of(pre);
            var preLavel = new Label("pre", preModel);
            item.add(preLavel);

            int pre_num = Integer.parseInt(pre);

            predictionService.registerPre(file.toString(), pre_num);
        }
    }

    private class UploadForm extends Form{
        public UploadForm(String id) {
            super(id);
            // FileUploadFieldを使う場合にFormに必要なセッティング
            setMultiPart(true);
            // ファイルサイズ制限の設定
            setMaxSize(Bytes.megabytes(2));

            uploadField = new FileUploadField("uploadField");
            add(uploadField);

            // アップロードするディレクトリ（場所）の取得
            uploadDir = getDirectory("upload");



            add(new Button("uploadButton"){
                @Override
                public void onSubmit(){
                    FileUpload upload = uploadField.getFileUpload();

                    if(upload == null){
                        // FeedbackPanelコンポーネントを使用する場合のメッセージ
                        error("アップロードするファイルが見つかりません");
                        return;
                    }

                    String fileName = upload.getClientFileName();

                    // 空ファイルをアップロードするディレクトリへ作成
                    File uploadFile = new File(uploadDir, fileName);

                    try{
                        // 空ファイルへアップロードファイルを書き出す
                        upload.writeTo(uploadFile);
                    }catch(Exception e){
                        this.error(e.getMessage());
                    }finally{
                        upload.closeStreams();
                    }
                    setResponsePage(UploadPage.class);
                }
            });
        }
    }
}