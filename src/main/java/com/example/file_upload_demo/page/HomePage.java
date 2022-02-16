package com.example.file_upload_demo.page;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;
import com.github.openjson.JSONObject;
import org.apache.wicket.markup.html.WebPage;
import org.wicketstuff.annotation.mount.MountPath;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

@WicketHomePage
@MountPath("Home")
public class HomePage extends WebPage {

    public HomePage() {
        var youModel = Model.of("Wicket-Spring-Boot");
        var youLabel = new Label("you", youModel);
        add(youLabel);


        String url = "http://127.0.0.1:5000/predict/XXXXXXXXXXX";
        getResult getresult = new getResult();
        String ans = getresult.getResult(url);
        JSONObject jsonObject = new JSONObject(ans);
        ans = jsonObject.getString("prediction");
        var testModel = Model.of(ans);
        var testLavel = new Label("test", testModel);
        add(testLavel);
    }

}