package org.xcolab.portlets.proposals.wrappers;

import com.ext.portlet.model.Contest;
import com.ext.portlet.model.FocusArea;
import com.ext.portlet.model.OntologyTerm;
import com.ext.portlet.model.Proposal;
import com.ext.portlet.service.FocusAreaLocalServiceUtil;
import com.ext.portlet.service.OntologyTermLocalServiceUtil;
import com.ext.portlet.service.ProposalLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A collection of ProposalImpactSeries representing all data series of each sector-region pair.
 *
 * Created by kmang on 12/03/15.
 */
public class ProposalImpactSeriesList {
    private List<ProposalImpactSeries> impactSerieses;

    private static final Map<Long, Map<Long,Double>> ONTOLOGY_REGION_TERM_TO_YEAR_TO_VALUE_FACTOR;
    private static final Double[] US_YEAR_TO_VALUE_FACTOR = {0.1832 , 0.1740 , 0.1592 , 0.1464 , 0.1348 , 0.1595 };
    private static final Double[] OTHER_DEVELOPED_YEAR_TO_VALUE_FACTOR = {0.1346 , 0.1273 , 0.1135 , 0.1023 , 0.0925 , 0.1140};
    private static final Double[] EU_YEAR_TO_VALUE_FACTOR = {0.1018 , 0.0974 , 0.0911 , 0.0854 , 0.0803 , 0.0912};
    private static final Double[] CHINA_YEAR_TO_VALUE_FACTOR = {0.2453 , 0.2616 , 0.2858 , 0.3042 , 0.3199 , 0.2834};
    private static final Double[] INDIA_YEAR_TO_VALUE_FACTOR = {0.0611 , 0.0651 , 0.0704 , 0.0736 , 0.0753 , 0.0691};
    private static final Double[] OTHER_DEVELOPING_YEAR_TO_VALUE_FACTOR = {0.2740 , 0.2747 , 0.2801 , 0.2881 , 0.2973 , 0.2828};
    private static final Integer[] YEARS_YEAR_TO_VALUE_FACTOR = {2015,2020,2030,2040,2050};

    static {
        Map<Long, Map<Long,Double>> ontologyRegionTermToYearToValueFactor =  new HashMap<>();
        Map<Long,Double> usMap = new HashMap<>();
        Map<Long,Double> euMap = new HashMap<>();
        Map<Long,Double> indiaMap = new HashMap<>();
        Map<Long,Double> chinaMap = new HashMap<>();
        Map<Long,Double> developedMap = new HashMap<>();
        Map<Long,Double> developingMap = new HashMap<>();

        for(int i = 0; i < YEARS_YEAR_TO_VALUE_FACTOR.length; i++){
            usMap.put(YEARS_YEAR_TO_VALUE_FACTOR[i].longValue(),US_YEAR_TO_VALUE_FACTOR[i]);
            euMap.put(YEARS_YEAR_TO_VALUE_FACTOR[i].longValue(),EU_YEAR_TO_VALUE_FACTOR[i]);
            indiaMap.put(YEARS_YEAR_TO_VALUE_FACTOR[i].longValue(),INDIA_YEAR_TO_VALUE_FACTOR[i]);
            chinaMap.put(YEARS_YEAR_TO_VALUE_FACTOR[i].longValue(),CHINA_YEAR_TO_VALUE_FACTOR[i]);
            developedMap.put(YEARS_YEAR_TO_VALUE_FACTOR[i].longValue(),OTHER_DEVELOPED_YEAR_TO_VALUE_FACTOR[i]);
            developingMap.put(YEARS_YEAR_TO_VALUE_FACTOR[i].longValue(),OTHER_DEVELOPING_YEAR_TO_VALUE_FACTOR[i]);
        }

        ontologyRegionTermToYearToValueFactor.put(1300340L, usMap);
        ontologyRegionTermToYearToValueFactor.put(1300341L, euMap);
        ontologyRegionTermToYearToValueFactor.put(1300345L, chinaMap);
        ontologyRegionTermToYearToValueFactor.put(1300346L, indiaMap);
        ontologyRegionTermToYearToValueFactor.put(1311101L, developedMap);
        ontologyRegionTermToYearToValueFactor.put(1311102L, developingMap);
        ONTOLOGY_REGION_TERM_TO_YEAR_TO_VALUE_FACTOR = Collections.unmodifiableMap(ontologyRegionTermToYearToValueFactor);
    }



    public ProposalImpactSeriesList(Proposal proposal) throws SystemException, PortalException {
        this (ProposalLocalServiceUtil.getLatestProposalContest(proposal.getProposalId()), proposal);
    }

    public ProposalImpactSeriesList(Contest contest, Proposal proposal)
            throws PortalException, SystemException {

        this.impactSerieses = new ArrayList<ProposalImpactSeries>();

        List<FocusArea> proposalFocusAreas = ProposalLocalServiceUtil.getImpactProposalFocusAreas(proposal);
        
        for (FocusArea focusArea : proposalFocusAreas) {
            // Get the impact series for the respective focus area
            this.impactSerieses.add(new ProposalImpactSeries(contest, proposal, focusArea));
        }

        Collections.sort(impactSerieses, new Comparator<ProposalImpactSeries>() {
            // Sort by whatTerm name and whereTerm name
            @Override
            public int compare(ProposalImpactSeries o1, ProposalImpactSeries o2) {
                int diff = o1.getWhatTerm().getName().compareTo(o2.getWhatTerm().getName());

                if (diff == 0) {
                    return o1.getWhereTerm().getName().compareTo(o2.getWhereTerm().getName());
                }

                return diff;
            }
        });
    }

    public void persistImpactSeriesesWithAuthor(User author) throws PortalException, SystemException {
        for (ProposalImpactSeries impactSeries : getImpactSerieses()) {
            impactSeries.persistWithAuthor(author);
        }
    }

    public List<ProposalImpactSeries> getImpactSerieses() {
        return impactSerieses;
    }

    public void addProposalImpactSeries(ProposalImpactSeries proposalImpactSeries) {
        // Check whether serie already exists
        for (ProposalImpactSeries loopedSeries : this.impactSerieses) {
            if (loopedSeries.getFocusArea().getId() == proposalImpactSeries.getFocusArea().getId()) {
                this.getImpactSerieses().remove(loopedSeries);
                break;
            }
        }

        this.impactSerieses.add(proposalImpactSeries);
    }

    public FocusArea getFocusAreaForTerms(OntologyTerm whatTerm, OntologyTerm whereTerm) {
        for (ProposalImpactSeries impactSeries : impactSerieses) {
            if (impactSeries.getWhatTerm().getId() == whatTerm.getId() &&
                    impactSeries.getWhereTerm().getId() == whereTerm.getId()) {

                return impactSeries.getFocusArea();
            }
        }

        return null;
    }

    /**
     * Returns a map containing aggregated proposal impact series values of all sector-region pairs of a proposal
     *
     * @param seriesTypes           seriesTypes that are being calculated
     * @return                      A map containing a ProposalImpactSeriesValues object for each impact series type
     * @throws SystemException
     * @throws PortalException
     */
    public Map<String, ProposalImpactSeriesValues> getAggregatedSeriesValues(List<String> seriesTypes) throws SystemException, PortalException {
        Map<String, ProposalImpactSeriesValues> seriesTypeToSeriesSumMap = new HashMap<>(seriesTypes.size());

        for (String seriesType : seriesTypes) {
            seriesTypeToSeriesSumMap.put(seriesType, new ProposalImpactSeriesValues());
        }
        seriesTypeToSeriesSumMap.put(ProposalImpactSeries.SERIES_TYPE_RESULT_KEY, new ProposalImpactSeriesValues());

        for (ProposalImpactSeries impactSeries : impactSerieses) {
            for (String seriesType : seriesTypes) {
                ProposalImpactSeriesValues integratedSeriesValues = seriesTypeToSeriesSumMap.get(seriesType);
                ProposalImpactSeriesValues impactSeriesValues = impactSeries.getSeriesValuesForType(seriesType);

                // Add up all the impact series values
                integratedSeriesValues.addImpactSeriesValues(impactSeriesValues);
            }

            // Aggregate result impact series
            ProposalImpactSeriesValues integratedSeriesValues = seriesTypeToSeriesSumMap.get(ProposalImpactSeries.SERIES_TYPE_RESULT_KEY);
            ProposalImpactSeriesValues impactSeriesValues = impactSeries.getResultSeriesValues();
            integratedSeriesValues.addImpactSeriesValues(impactSeriesValues);
        }

        return seriesTypeToSeriesSumMap;
    }

    /**
     * Returns a map containing aggregated proposal impact series values of all sector-region pairs of a proposal
     *
     * @param seriesTypes           seriesTypes that are being calculated
     * @return                      A map containing a ProposalImpactSeriesValues object for each impact series type
     * @throws SystemException
     * @throws PortalException
     */
    public Map<String, ProposalImpactSeriesValues> getAggregatedSeriesValues(List<String> seriesTypes, OntologyTerm regionOntologyTerm) throws SystemException, PortalException {
        Map<String, ProposalImpactSeriesValues> seriesTypeToSeriesSumMap = new HashMap<>(seriesTypes.size());

        for (String seriesType : seriesTypes) {
            seriesTypeToSeriesSumMap.put(seriesType, new ProposalImpactSeriesValues());
        }
        seriesTypeToSeriesSumMap.put(ProposalImpactSeries.SERIES_TYPE_RESULT_KEY, new ProposalImpactSeriesValues());

        for (ProposalImpactSeries impactSeries : impactSerieses) {
            if(impactSeries.getWhereTerm().equals(regionOntologyTerm)) {
                for (String seriesType : seriesTypes) {
                    ProposalImpactSeriesValues integratedSeriesValues = seriesTypeToSeriesSumMap.get(seriesType);
                    ProposalImpactSeriesValues impactSeriesValues = impactSeries.getSeriesValuesForType(seriesType);

                    // Add up all the impact series values
                    integratedSeriesValues.addImpactSeriesValues(impactSeriesValues);
                }

                // Aggregate result impact series
                ProposalImpactSeriesValues integratedSeriesValues = seriesTypeToSeriesSumMap.get(ProposalImpactSeries.SERIES_TYPE_RESULT_KEY);
                ProposalImpactSeriesValues impactSeriesValues = impactSeries.getResultSeriesValues();
                integratedSeriesValues.addImpactSeriesValues(impactSeriesValues);
            }
        }

        return seriesTypeToSeriesSumMap;
    }

    /**
     * Returns a map containing aggregated proposal impact series values of all sector-region pairs of a proposal
     *
     * @param seriesTypes           seriesTypes that are being calculated
     * @return                      A map containing a ProposalImpactSeriesValues object for each impact series type
     * @throws SystemException
     * @throws PortalException
     */
    public Map<String, ProposalImpactSeriesValues> getAggregatedSeriesValues(List<String> seriesTypes, Long regionOntologyTermId, List<Long> sectorOntologyTermIds) throws SystemException, PortalException {

        Map<String, ProposalImpactSeriesValues> seriesTypeToSeriesSumMap = new HashMap<>(seriesTypes.size());
        OntologyTerm regionOntologyTerm = OntologyTermLocalServiceUtil.getOntologyTerm(regionOntologyTermId);

        for (String seriesType : seriesTypes) {
            seriesTypeToSeriesSumMap.put(seriesType, new ProposalImpactSeriesValues());
        }

        for (Long ontologyTermId : sectorOntologyTermIds) {
            seriesTypeToSeriesSumMap.put(ontologyTermId.toString(), new ProposalImpactSeriesValues());
        }

        seriesTypeToSeriesSumMap.put(ProposalImpactSeries.SERIES_TYPE_RESULT_KEY, new ProposalImpactSeriesValues());
        ProposalImpactSeriesValues emptySeries = new ProposalImpactSeriesValues();

        for (ProposalImpactSeries impactSeries : impactSerieses) {

            if(impactSeries.getWhereTerm().equals(regionOntologyTerm)) {

                ProposalImpactSeriesValues impactSeriesValues = impactSeries.getResultSeriesValues();

                ProposalImpactSeriesValues integratedSeriesValues = seriesTypeToSeriesSumMap.get(ProposalImpactSeries.SERIES_TYPE_RESULT_KEY);
                integratedSeriesValues.addImpactSeriesValues(impactSeriesValues);

                for (String seriesType : seriesTypes) {
                    integratedSeriesValues = seriesTypeToSeriesSumMap.get(seriesType);
                    impactSeriesValues = impactSeries.getSeriesValuesForType(seriesType);
                    integratedSeriesValues.addImpactSeriesValues(impactSeriesValues);
                }

                for (Long sectorOntologyTermId : sectorOntologyTermIds) {
                    OntologyTerm sectorOntologyTerm = OntologyTermLocalServiceUtil.getOntologyTerm(sectorOntologyTermId);
                    integratedSeriesValues = seriesTypeToSeriesSumMap.get(sectorOntologyTermId.toString());
                    if (!(impactSeries.getWhatTerm().equals(sectorOntologyTerm))){
                        for (Integer year : impactSeriesValues.getYearToValueMap().keySet()) {
                            emptySeries.putSeriesValue(year, 0.);
                        }
                        integratedSeriesValues.addImpactSeriesValues(emptySeries);
                    } else {
                        integratedSeriesValues.addImpactSeriesValues(impactSeriesValues);
                    }
                }
            }
        }

        return seriesTypeToSeriesSumMap;
    }

    public ProposalImpactSeriesValues getAggregatedSeriesValuesByOntologyTermId(Long ontologyTermId) throws SystemException, PortalException {

        ProposalImpactSeriesValues aggregatedSeriesValuesByOntologyTermId = new ProposalImpactSeriesValues();

        OntologyTerm ontologyTerm = OntologyTermLocalServiceUtil.getOntologyTerm(ontologyTermId);
        ProposalImpactSeriesValues emptySeries = new ProposalImpactSeriesValues();
        for (ProposalImpactSeries impactSeries : impactSerieses) {

                ProposalImpactSeriesValues impactSeriesValues = impactSeries.getResultSeriesValues();
            if(!impactSeries.getWhatTerm().equals(ontologyTerm)) {
                for(Integer year : impactSeriesValues.getYearToValueMap().keySet()){
                    emptySeries.putSeriesValue(year, 0.);
                }
                aggregatedSeriesValuesByOntologyTermId.addImpactSeriesValues(emptySeries);
            } else {
                aggregatedSeriesValuesByOntologyTermId.addImpactSeriesValues(impactSeriesValues);
            }
        }

        return aggregatedSeriesValuesByOntologyTermId;
    }



    public Map<String, ProposalImpactSeriesValues> getAggregatedSeriesValuesByRegionAndSectorOntologyTermIds(Long regionOntologyTermId, List<Long> sectorOntologyTermIds) throws SystemException, PortalException {
        Map<String, ProposalImpactSeriesValues> ontologyTermIdToSeriesSumMap = new HashMap<>(sectorOntologyTermIds.size());
        OntologyTerm regionOntologyTerm = OntologyTermLocalServiceUtil.getOntologyTerm(regionOntologyTermId);

        for (Long ontologyTermId : sectorOntologyTermIds) {
            ontologyTermIdToSeriesSumMap.put(ontologyTermId.toString(), new ProposalImpactSeriesValues());
        }

        ProposalImpactSeriesValues emptySeries = new ProposalImpactSeriesValues();

        for (ProposalImpactSeries impactSeries : impactSerieses) {

            ProposalImpactSeriesValues impactSeriesValues = impactSeries.getResultSeriesValues();

            for (String sectorOntologyTermId : ontologyTermIdToSeriesSumMap.keySet()) {

                OntologyTerm sectorOntologyTerm = OntologyTermLocalServiceUtil.getOntologyTerm(Long.parseLong(sectorOntologyTermId));
                ProposalImpactSeriesValues integratedSeriesValues = ontologyTermIdToSeriesSumMap.get(sectorOntologyTermId);

                if (!(impactSeries.getWhatTerm().equals(sectorOntologyTerm) && impactSeries.getWhereTerm().equals(regionOntologyTerm))){
                    for (Integer year : impactSeriesValues.getYearToValueMap().keySet()) {
                        emptySeries.putSeriesValue(year, 0.);
                    }
                    integratedSeriesValues.addImpactSeriesValues(emptySeries);
                } else {
                    integratedSeriesValues.addImpactSeriesValues(impactSeriesValues);
                }
            }
        }

        return ontologyTermIdToSeriesSumMap;
    }

    public Map<String, ProposalImpactSeriesValues> getAggregatedWeightedSeriesValuesBySectorOntologyTermIds(List<Long> sectorOntologyTermIds) throws SystemException, PortalException {
        Map<String, ProposalImpactSeriesValues> ontologyTermIdToSeriesSumMap = new HashMap<>(sectorOntologyTermIds.size());

        for (Long ontologyTermId : sectorOntologyTermIds) {
            ontologyTermIdToSeriesSumMap.put(ontologyTermId.toString(), new ProposalImpactSeriesValues());
        }

        ProposalImpactSeriesValues emptySeries = new ProposalImpactSeriesValues();

        for (ProposalImpactSeries impactSeries : impactSerieses) {

            ProposalImpactSeriesValues impactSeriesValues = impactSeries.getResultSeriesValues();

            for (String sectorOntologyTermId : ontologyTermIdToSeriesSumMap.keySet()) {

                OntologyTerm sectorOntologyTerm = OntologyTermLocalServiceUtil.getOntologyTerm(Long.parseLong(sectorOntologyTermId));
                ProposalImpactSeriesValues integratedSeriesValues = ontologyTermIdToSeriesSumMap.get(sectorOntologyTermId);

                if (!(impactSeries.getWhatTerm().equals(sectorOntologyTerm))){
                    for (Integer year : impactSeriesValues.getYearToValueMap().keySet()) {
                        emptySeries.putSeriesValue(year, 0.);
                    }
                    integratedSeriesValues.addImpactSeriesValues(emptySeries);
                } else {
                    OntologyTerm ontologyRegionTerm = impactSeries.getWhereTerm();
                    Map<Long, Double> yearToValueFactor = ONTOLOGY_REGION_TERM_TO_YEAR_TO_VALUE_FACTOR.get(ontologyRegionTerm.getPrimaryKey());
                    integratedSeriesValues.addImpactSeriesValues(impactSeriesValues, yearToValueFactor);
                }
            }
        }

        return ontologyTermIdToSeriesSumMap;
    }
}
