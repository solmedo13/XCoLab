package org.xcolab.performance;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

class ThreadTestGroup extends Thread {

    private final CloseableHttpClient httpClient;
    private final HttpContext context;
    private final Integer testGroupId;
    private Date startDateTime;
    private Date endDateTime;
    private Long elapsedTime;
    private Random randomGenerator;

    private ArrayList<HttpRequestStep> testSteps;

    public ThreadTestGroup(CloseableHttpClient httpClient, int testGroupId) {
        this.httpClient = httpClient;
        this.context = HttpClientContext.create();
        this.testGroupId = testGroupId;
        this.testSteps = new ArrayList<HttpRequestStep>();
        this.randomGenerator = new Random();
    }

    @Override
    public void run() {
        String server = "http://colab2.mit.edu:18082";
        try {
            startDateTime = new Date();
            testSteps.add(
                    new HttpRequestStep(
                            server, "home", "/",
                            HttpRequestStep.GET,  this.httpClient, this.context).runStep());
            testSteps.add(
                    new HttpRequestStep(server, "contests", "/contests",
                            HttpRequestStep.GET,  this.httpClient, this.context).runStep());
            testSteps.add(
                    new HttpRequestStep(server, "register", "/register",
                            HttpRequestStep.GET,  this.httpClient, this.context).runStep());
            testSteps.add(
                    new HttpRequestStep(server, "registered", "/register",
                            HttpRequestStep.POST, getRegisterParams()
                            , this.httpClient, this.context).runStep());

            testSteps.add(
                    new HttpRequestStep(server, "contestpage",
                            "/contests/2017/A2R-Anticipating-Climate-Hazards", HttpRequestStep.GET,
                            null
                            , this.httpClient, this.context).runStep());

            long randomPropId = getRandomProposalId();
            testSteps.add(
                    new HttpRequestStep(server, "proposalpage",
                            "/contests/2017/A2R-Anticipating-Climate-Hazards/c/proposal/"+randomPropId+"",
                            HttpRequestStep.GET,
                            null
                            , this.httpClient, this.context).runStep());

            testSteps.add(
                    new HttpRequestStep(server, "voteforproposal",
                            "/contests/2017/A2R-Anticipating-Climate-Hazards/c/proposal/"+randomPropId+"/voteOnProposalAction",
                            HttpRequestStep.GET,
                            null
                            , this.httpClient, this.context).runStep());


            endDateTime = new Date();
            elapsedTime = endDateTime.getTime() - startDateTime.getTime();
            printStatusReport();
        } catch (ClientProtocolException ex) {
            // Handle protocol errors
        } catch (IOException ex) {
            // Handle I/O errors
        }
    }
    public void printStatusReport(){

        int numberOfRequest = this.testSteps.size();
        System.out.println("");
        System.out.println("");
        System.out.println("Finished with all  " + numberOfRequest + " test steps");
        System.out.println("Elapsed time:" + this.elapsedTime + " ms");
        System.out.println("Request throughput:" + numberOfRequest/this.elapsedTime + " requests/ms");
        long totalBytes = 0;
        for(HttpRequestStep hrs: this.testSteps) {
            totalBytes += hrs.getContentLength();
        }
        System.out.println("Data throughput:" + totalBytes/this.elapsedTime + " requests/ms");
    }

    private Long getRandomProposalId(){
        Long[] proposals = {1333807l,1333790l,1333732l,1333837l,1333850l,1333813l,1333103l,1333303l,1333804l,1333801l,1333848l,1333815l,1333846l,1333773l,1333840l,1333845l,1333844l,1333843l,1333842l,1333753l,1333828l,1333841l,1333839l,1333832l,1333836l,1333838l,1333817l,1333821l,1333830l,1333826l,1333823l,1333803l,1333810l,1333835l,1333834l,1333791l,1333831l,1333819l,1333820l,1333829l,1333827l,1333825l,1333822l,1333814l,1333818l,1333778l,1333782l,1333789l,1333812l,1333802l,1333805l,1333769l,1333809l,1333703l,1333808l,1333001l,1326512l,1333806l,1333797l,1333800l,1333795l,1333799l,1333798l,1333731l,1333796l,1333793l,1333794l,1333707l,1333792l,1333723l,1333786l,1333783l,1333721l,1333777l,1333774l,1333772l,1333727l,1333501l,1333709l,1333717l,1333202l,1333603l,1333701l,1333705l,1333604l,1326511l,1326508l,1326602l,1326704l,1326905l,1326908l,1327402l,1327804l,330115l};
        int index = randomGenerator.nextInt(proposals.length);
        return proposals[index];
    }
    private Map<String, String> getRegisterParams() {
        Map<String, String> map = new HashMap<String, String>();
        String screenName = "testuser"+this.testGroupId+"_{threadId}";
        map.put("screenName", screenName);
        map.put("email", screenName+"@gmail.com");
        map.put("retypeEmail", screenName+"@gmail.com");
        map.put("firstName", "Test");
        map.put("lastName", "User");
        map.put("password", "colab1234");
        map.put("retypePassword", "colab1234");
        map.put("country", "AF");
        map.put("shortBio", "<p>Test user </p>");
        map.put("imageId", "");

        return map;
    }
}