package org.xcolab.portlets.contestmanagement.utils;

import au.com.bytecode.opencsv.CSVWriter;
import com.ext.portlet.model.*;
import com.ext.portlet.service.*;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import org.xcolab.wrapper.proposal.ProposalTeamMemberWrapper;
import org.xcolab.wrapper.proposal.ProposalWrapper;

import javax.portlet.*;
import javax.sound.sampled.Port;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Thomas on 3/25/2015.
 */
public class CsvExportUtil {

    private final static Log _log = LogFactoryUtil.getLog(CsvExportUtil.class);
    private final String URL_DOMAIN = "http://www.climatecolab.org";
    private List<String[]> records;


    public CsvExportUtil() {
        records = new ArrayList<String[]>();
    }

    private String getCSVData() throws Exception {
        StringWriter writer = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.DEFAULT_QUOTE_CHARACTER);
        csvWriter.writeAll(records);
        csvWriter.close();
        return writer.toString();
    }

    public void addRowToExportData(List<String> rowData){
        String[] rowDataArr = new String[rowData.size()];
        rowDataArr = rowData.toArray(rowDataArr);
        addRowToExportData(rowDataArr);
    }

    private void addRowToExportData(String[] rowData) {
        records.add(rowData);
    }

    public void addProposalAndAuthorDetailsToExportData(List<Proposal> proposals) throws Exception{

        try {
            for (Proposal proposal : proposals) {
                List<String[]> proposalAndAuthorDetailsRows = generateProposalAndAuthorDetailsRows(proposal);
                records.addAll(proposalAndAuthorDetailsRows);
            }
        } catch (Exception e){
            _log.warn("Failed to export data for csv: ", e);
        }

    }

    private List<String[]> generateProposalAndAuthorDetailsRows(Proposal proposal) throws Exception{
        List<String[]> proposalExportData = new ArrayList<>();
        ProposalWrapper proposalWrapper = new ProposalWrapper(proposal);
        Long contestId = proposalWrapper.getContestPK();
        Contest contest = ContestLocalServiceUtil.getContest(contestId);
        String contestTitle = contest.getContestShortName();
        String proposalTitle = proposalWrapper.getName();
        String proposalLink = URL_DOMAIN + proposalWrapper.getProposalURL();
        String lastPhaseTitle = getLastContestPhaseForProposal(proposal);

        List<ProposalTeamMemberWrapper> proposalTeam = proposalWrapper.getMembers();
        for(ProposalTeamMemberWrapper teamMemberWrapper : proposalTeam){
            String[] csvRow = generateProposalAndUserDetailsRow(contestTitle, proposalTitle, proposalLink, teamMemberWrapper, lastPhaseTitle);
            proposalExportData.add(csvRow);
        }
        return proposalExportData;
    }

    private String getLastContestPhaseForProposal(Proposal proposal) throws Exception{
        ProposalVersion proposalVersion =
                ProposalVersionLocalServiceUtil.getByProposalIdVersion(proposal.getProposalId(), proposal.getCurrentVersion());
        Proposal2Phase proposal2Phase = Proposal2PhaseLocalServiceUtil.getForVersion(proposalVersion);
        Long contestPhaseId = proposal2Phase.getContestPhaseId();
        ContestPhase contestPhase =  ContestPhaseLocalServiceUtil.getContestPhase(contestPhaseId);
        Long contestPhaseTypeId = contestPhase.getContestPhaseType();
        ContestPhaseType contestPhaseType = ContestPhaseTypeLocalServiceUtil.getContestPhaseType(contestPhaseTypeId);
        return contestPhaseType.getName();
    }

    private String[] generateProposalAndUserDetailsRow(String contestTitle, String proposalTitle,
                                                       String proposalLink, ProposalTeamMemberWrapper member,
                                                       String lastPhaseTitle) throws Exception{
        User user = UserLocalServiceUtil.getUser(member.getUserId());
        String username = user.getScreenName();
        String firstName = user.getFullName();
        String lastName = user.getLastName();
        String emailAddress = user.getEmailAddress();
        String role = member.getMemberType();

        return new String[]{contestTitle
                                ,proposalTitle
                                ,proposalLink
                                ,username
                                ,firstName
                                ,lastName
                                ,emailAddress
                                ,role
                                ,lastPhaseTitle};

    }

    public void initiateDownload(String downloadFileName, PortletRequest request, ResourceResponse response) throws Exception{

        ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(WebKeys.THEME_DISPLAY);
        ServiceContext serviceContext = new ServiceContext();
        serviceContext.setPortalURL(themeDisplay.getPortalURL());

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            String csvPayload = getCSVData();
            outputStream.write(csvPayload.getBytes());

            response.setContentType("application/csv");
            response.addProperty(HttpHeaders.CACHE_CONTROL, "max-age=3600, must-revalidate");
            response.setProperty(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + downloadFileName + ".csv");
            response.setContentLength(outputStream.size());
            OutputStream out = response.getPortletOutputStream();
            outputStream.writeTo(out);
            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}