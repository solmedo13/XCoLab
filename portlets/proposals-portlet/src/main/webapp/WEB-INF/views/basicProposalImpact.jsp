<jsp:root xmlns:c="http://java.sun.com/jsp/jstl/core"
          xmlns:jsp="http://java.sun.com/JSP/Page"
          xmlns:fn="http://java.sun.com/jsp/jstl/functions"
          xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
          xmlns:spring="http://www.springframework.org/tags"
          xmlns:form="http://www.springframework.org/tags/form"
          xmlns:proposalsPortlet="urn:jsptagdir:/WEB-INF/tags/proposalsPortlet"
          xmlns:liferay-ui="http://liferay.com/tld/ui"
          xmlns:portlet="http://java.sun.com/portlet_2_0" version="2.0">
    <jsp:directive.include file="./init.jspx" />

    <jsp:directive.include file="./proposalDetails/header.jspx" />

    <portlet:resourceURL var="getRegionsUrl" id="proposalImpactGetRegions">
    </portlet:resourceURL>

    <portlet:resourceURL var="getSectorsForRegionURL" id="proposalImpactGetSectorsForRegion">
        <portlet:param name="regionTermId" value="@@REPLACE-REGION_TERM_ID@@" />
    </portlet:resourceURL>

    <portlet:resourceURL var="getDataSeriesURL" id="proposalImpactGetDataSeries">
        <portlet:param name="sectorTermId" value="@@REPLACE-SECTOR_TERM_ID@@" />
        <portlet:param name="regionTermId" value="@@REPLACE-REGION_TERM_ID@@" />
    </portlet:resourceURL>

    <portlet:resourceURL var="saveDataSeriesURL" id="proposalImpactSaveDataSeries">
        <portlet:param name="focusAreaId" value="@@REPLACE-FOCUS_AREA_ID@@" />
    </portlet:resourceURL>

    <portlet:resourceURL var="deleteDataSeriesURL" id="proposalImpactDeleteDataSeries">
        <portlet:param name="focusAreaId" value="@@REPLACE-FOCUS_AREA_ID@@" />
    </portlet:resourceURL>

    <script>
        // Placeholder strings
        var SECTOR_TERM_ID_PLACEHOLDER = "SECTOR_TERM_ID";
        var REGION_TERM_ID_PLACEHOLDER = "REGION_TERM_ID";
        var FOCUS_AREA_ID_PLACEHOLDER = "FOCUS_AREA_ID";

        var IMPACT_REDUCTION_PLACEHOLDER = "IMPACT_REDUCTION";
        var IMPACT_ADOPTION_RATE_PLACEHOLDER = "IMPACT_ADOPTION_RATE";

        // URL strings
        var getRegionsURL = '${getRegionsUrl}';
        var getSectorsForRegionURL = '${getSectorsForRegionURL}';
        var getDataSeriesURL = '${getDataSeriesURL}';
        var saveDataSeriesURL = '${saveDataSeriesURL}';
        var deleteDataSeriesURL = '${deleteDataSeriesURL}';
    </script>

    <!-- HTML templates -->
    <script id="impactSeriesEditTableRowTemplate" type="text/x-handlebars-template">
        <tr id="impact-edit-row-{{series.name}}" class="impact-edit-row">
            <td width="45%">{{series.description}}
                <div class="addprophelp"><!-- Todo replace dummy text {{series.helpText}}--> dummy text </div>
            </td>
            {{#each series.values}}
                {{#if ../series.editable}}
                    <td class="impact-value">
                        <input type="text" name="{{this.year}}" value="{{this.value}}" class="series-value" onClick="this.setSelectionRange(0, this.value.length)"/>
                        <!-- Using % as unit, since we only use it till now; may need refinement in the future -->
                        <span style="margin-left: 3px">%</span>
                    </td>
                {{else}}
                    <td class="impact-value">
                        <span class="series-value">{{this.value}}</span>
                    </td>
                {{/if}}
            {{/each}}
        </tr>
    </script>

    <script id="impactSeriesSaveButtonTemplate" type="text/x-handlebars-template">
        <div class="edit-prop-butts">
            <a id="impact-edit-save-button" href="javascript:;">Save</a>
        </div>
        <span class="spinner-area">&#160;</span>
    </script>

    <script id="impactSeriesNewTableRowTemplate" type="text/x-handlebars-template">
        <tr class="impact-series-clickable" id="impact-row-{{series.rowIndex}}">
            <td class="region"><span id="{{series.regionTerm.id}}">{{series.regionTerm.name}}</span></td>
            <td class="sector"><span id="{{series.sectorTerm.id}}">{{series.sectorTerm.name}}</span></td>
            {{#each series.resultValues}}
                <td class="impact-value">{{this}}</td>
            {{/each}}
        </tr>
    </script>

    <script id="impactSeriesAuthorTemplate" type="text/x-handlebars-template">
        <a href="/web/guest/member/-/member/userId/{{author.userId}}">{{author.name}}</a>
    </script>


    <!-- Content -->
    <div id="content">
        <div id="impact">
            <div style="margin-bottom: 15px">
                <strong>How will this proposal reduce greenhouse gas emissions? </strong>The proposal authors can use the tools below or work with the
                <a href="/web/guest/resources/-/wiki/Main/Climate+CoLab+Impact+Assessment+Fellows">Impact Assessment Fellows</a>
                to estimate the greenhouse gas (GHG) reductions the proposal will cause in different economic sectors around the world.
                Once calculated, the top table will show the GHG emissions reductions from 2020 to 2050 for that sector and region.
            </div>
            <c:choose>
                <!-- Don't show table if no data is available and if user cannot edit -->
                <!-- TODO change this back to proposalsPermission.canEdit when officially launching the feature -->
                <c:when test="${(not proposalsPermissions.canEdit) and (not proposalsPermissions.canFellowActions) and (empty impactSerieses)}">
                    <h3>No data available yet.</h3>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead>
                            <tr>
                                <th style="width: 22.5%;"><!-- --></th>
                                <th style="width: 22.5%;"><!-- --></th>
                                <th class="blue-bg" style="width: 55%; text-align: center">Proposal’s Greenhouse Gas Emission Reductions, Per Decade [GtCO2e]</th>
                            </tr>
                        </thead>
                    </table>
                    <table id="impact-summary">
                        <thead>
                            <tr>
                                <th class="blue-bg" style="width: 22.5%;">Sector</th><th class="blue-bg" style="width: 22.5%;">Region</th>
                                <c:forEach var="impactIteration" items="${impactIterations}">
                                    <th class="blue-bg" style="text-align: center; width: ${55.0 / fn:length(impactIterations)}%">
                                        ${impactIteration.year}
                                        <div class="addprophelp"><!-- Todo replace dummy text --> help text</div>
                                        <div class="clearfix"><!-- --></div>
                                    </th>
                                </c:forEach>
                            </tr>
                        </thead>
                        <tbody id="summary-body">
                            <c:forEach var="impactSeries" items="${impactSerieses}" varStatus="index">
                                <tr class="impact-series-clickable" id="impact-row-${index.index}">
                                    <td class="region"><span id="${impactSeries.whereTerm.id}">${impactSeries.whereTerm.name}</span></td>
                                    <td class="sector"><span id="${impactSeries.whatTerm.id}">${impactSeries.whatTerm.name}</span></td>
                                    <c:forEach var="impactIteration" items="${impactIterations}">
                                        <td class="impact-value">${impactSeries.resultSeriesValues.yearToValueMap[impactIteration.year]}</td>
                                    </c:forEach>
                                </tr>
                            </c:forEach>
                        </tbody>
                        <c:if test="${proposalsPermissions.canEdit}">
                            <tbody>
                            <tr id="impact-new-series-row">
                                <td class="region" style="border-right: none;"><span><![CDATA[&lt;new region&gt;]]></span></td>
                                <td class="sector" style="border-left: none;"><span><![CDATA[&lt;new sector&gt;]]></span></td>
                                <c:forEach var="impactIteration" items="${impactIterations}">
                                    <td class="impact-value"><!-- --></td>
                                </c:forEach>
                            </tr>
                            </tbody>
                        </c:if>
                        <!--<div class="ui-resizable-handle" id="sgrip"> -->
                            <!-- -->
                        <!-- </div> -->
                    </table>
                    <div id="impact-series-detail" style="display:none">
                        <!-- New impact series -->
                        <div style="min-height: 70px; display: table; width: 100%">
                            <div id="new-series" class="shaded-bg" style="display: none;">
                                <div>
                                    <select id="region">
                                        <option value="0" selected="selected">Select region</option>
                                        <c:forEach var="regionTermEntry" items="${regionTerms}">
                                            <option value="${regionTermEntry.id}">
                                                    ${regionTermEntry.name}
                                            </option>
                                        </c:forEach>
                                    </select>
                                    <select id="sector">
                                        <option value="0" selected="selected">Select sector</option>
                                    </select>
                                    <div class="blue-button disabled">
                                        <a id="continue-button" href="javascript:;">Add</a>
                                    </div>
                                    <span class="spinner-area"><!-- --> </span>
                                </div>
                            </div>
                            <div id="header" class="shaded-bg" style="display: none">
                                <h3 id="region-title"><!-- --></h3>
                                <h3 id="sector-title"><!-- --></h3>
                                <div class="blue-button disabled" style="float:right; margin-right: 20px;">
                                    <a id="delete-button" href="javascript:;">Delete</a>
                                </div>
                                <div style="float:right; margin-right: 5px;" class="blue-button">
                                    <a id="save-button" href="javascript:;">Save</a>
                                </div>
                                <div id="author-info" style="margin-top: 2px; display: none;">
                                    <strong>Last saved:</strong>&#160; <span id="author-name"><!-- --></span> on <span id="save-date"><!-- --></span>
                                </div>
                            </div>
                        </div>

                        <table id="edit-table">
                            <thead>
                                <tr style="display: none;">
                                    <th style="width: 45%"><!-- --></th>
                                    <c:forEach var="impactIteration" items="${impactIterations}">
                                        <th style="width: ${55.0 / fn:length(impactIterations)}%"><!-- --></th>
                                    </c:forEach>
                                </tr>
                            </thead>
                            <tbody>
                                <!-- -->
                            </tbody>
                        </table>
                    </div>

                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <!-- TODO remove fellow permission -->
    <c:choose>
        <c:when test="${not proposalsPermissions.canEdit and not proposalsPermissions.canFellowActions}">
            <script>
                $().ready(function() {
                    $('tr.impact-series-clickable').removeClass('impact-series-clickable');
                });
            </script>
        </c:when>
        <c:otherwise>
            <script>
                $().ready(function() {
                    newSeriesRowClicked();
                });
            </script>
        </c:otherwise>
    </c:choose>

    <script>
        var ROW_INDEX_NONE_SELECTED = -1;
        var editedFocusArea = 0;
        var userInputOccurred = false;
        var currentEditingRowIndex = ROW_INDEX_NONE_SELECTED;
        var newSeriesModeEnabled = false;

        var newRegionTermId = 0;
        var newSectorTermId = 0;

        jQuery(document).ready(function() {
            registerEventHandler();

            // Init dependencies
            $('table#impact-summary').scrolltable({
                maxHeight: 300
            });

            // Hack
            $('table#impact-summary div.st-body-scroll').css('max-height', '308px');
            // Fix table width
            $('table#impact-summary .st-head-table').css('width', $('table#impact-summary .st-body-scroll').css('width'))

            // Format summary-tables's impact values
            $('table#impact-summary tbody#summary-body td.impact-value').each(function(idx) {
                $(this).text(numeral($(this).text()).format('0,0.00'));
            });

            /*
            var sgrip = $('div#sgrip');
            $('table#impact-summary').append(sgrip);
            $('table#impact-summary table.st-body-scroll').resizable({
                handles: {
                    's': '#sgrip'
                }
            });

            $('table#impact-summary div.st-body-scroll').append(sgrip);
            */
        });

        function registerEventHandler() {
            $('tr.impact-series-clickable').click(function() {
                console.log("clicked row " + $(this).attr('id'));
                toggleEditMode($(this));
            });

            $('a#delete-button').click(deleteSeriesRow);
            $('tr#impact-new-series-row').click(newSeriesRowClicked);

            $('a#continue-button').click(continueButtonClicked);
            $('a#save-button').click(saveDataSeries);

            // Change value in new series sector dropdown
            registerDropdownEventListener($('div#new-series select#region'), $('div#new-series select#sector'));
        }

        function disableEditMode() {
            if (currentEditingRowIndex != ROW_INDEX_NONE_SELECTED) {
                toggleEditMode(getSelectedOverviewTableRow());
            } else {
                dismissDetailView();
                //$('div#impact-series-detail').fadeOut('normal');
                userInputOccurred = false;
            }
        }

        function toggleEditMode(row) {
            var rowIndex = getIndexFromTableRow(row);
            row.addClass("selected");

            // Another row has already been edited
            if (currentEditingRowIndex != ROW_INDEX_NONE_SELECTED) {
                var oldEditingRow = getSelectedOverviewTableRow();
                oldEditingRow.removeClass("selected");
            }

            if (rowIndex == currentEditingRowIndex) {
                currentEditingRowIndex = ROW_INDEX_NONE_SELECTED;
                dismissDetailView();
                row.removeClass("selected");
                userInputOccurred = false;
                editedFocusArea = 0;
                currentEditingRowIndex = ROW_INDEX_NONE_SELECTED;
                $('#delete-button').parent().addClass('disabled');

                return;
            }

            $('tr#impact-new-series-row').removeClass('selected');
            currentEditingRowIndex = rowIndex;
            $('#delete-button').parent().removeClass('disabled');
            // Set detail table header
            var sectorTerm = row.find('td.sector > span');
            var regionTerm = row.find('td.region > span');
            $('div#impact-series-detail #header h3#region-title').text(regionTerm.text());
            $('div#impact-series-detail #header h3#sector-title').text(sectorTerm.text());

            var url = replaceImpactURLPlaceholders(getDataSeriesURL,
                    [SECTOR_TERM_ID_PLACEHOLDER, REGION_TERM_ID_PLACEHOLDER],
                    [sectorTerm.attr('id'), regionTerm.attr('id')]);
            console.log("load data series with url: " + url);
            loadSeriesEditData(url, row);

            // Show tables
            showEditTable();
        }

        function recalculateEditSeriesValues() {
            var bauValues = $('#impact table tr#impact-edit-row-BAU td span');
            var reductionValues = $('#impact table tr#impact-edit-row-'+ IMPACT_REDUCTION_PLACEHOLDER +' input');
            var adoptionValues = $('#impact table tr#impact-edit-row-'+ IMPACT_ADOPTION_RATE_PLACEHOLDER + ' input');
            var resultValues = $('#impact table tr#impact-edit-row-RESULT td span');

            for (var i = 0; i &lt; bauValues.size(); i++) {
                var resultValue = numeral((numeral().unformat($(bauValues[i]).text()) * (parseFloat($(reductionValues[i]).attr('value')) * 0.01 *
                        parseFloat($(adoptionValues[i]).attr('value')) * 0.01))).format('0,0.00');
                $(resultValues[i]).text('' + resultValue);
            }
        }
        /* Replace the URL placeholders with actual values */
        function replaceImpactURLPlaceholders(rawUrl, ids, values){
            var url = rawUrl;
            for (var i = 0; i &lt; ids.length; i++) {
                url = url.replace('%40%40REPLACE-' + ids[i] + '%40%40', values[i]);
            }

            return url;
        }

        function loadSeriesEditData(url) {
            setDetailViewSpinnerMode(true);
            $.getJSON(url, { get_param: 'value' }, function(data) {
                setDetailViewSpinnerMode(false);
                editedFocusArea = data.focusAreaId;

                // Delete old edit rows
                var editTable = $('div#impact-series-detail table#edit-table tbody');
                editTable.empty();

                // Add new rows
                console.log("json: " + JSON.stringify(data));
                var dataSeries = data.serieses.BAU;
                var tableRow = jQuery(impactSeriesEditTableRowTemplate({series: dataSeries}));
                tableRow.find('span.series-value').each(function(idx) {
                    $(this).text(numeral($(this).text()).format('0,0.00'))
                });
                editTable.append(tableRow);
                console.log("bau series"  + dataSeries);


                dataSeries = data.serieses.IMPACT_REDUCTION;
                tableRow = jQuery(impactSeriesEditTableRowTemplate({series: dataSeries}));
                console.log("Reduction: " + JSON.stringify(dataSeries));
                tableRow.find('span.series-value').each(function(idx) {
                    $(this).text(numeral($(this).text()).format('0,0.00'))
                });
                editTable.append(tableRow);
                console.log("reduction series"  + dataSeries);

                dataSeries = data.serieses.IMPACT_ADOPTION_RATE;
                tableRow = jQuery(impactSeriesEditTableRowTemplate({series: dataSeries}));
                tableRow.find('span.series-value').each(function(idx) {
                    $(this).text(numeral($(this).text()).format('0,0.00'))
                });
                editTable.append(tableRow);
                console.log("adoption series"  + dataSeries);

                dataSeries = {"name": "RESULT", "description": "Estimated emission reduction from this proposal [GtCO2e]", "editable": false,
                "values": dataSeries.values};
                tableRow = jQuery(impactSeriesEditTableRowTemplate({series: dataSeries}));
                tableRow.find('span.series-value').each(function(idx) {
                    $(this).text(numeral(parseFloat($(this).text())).format('0,0.00'));
                });
                tableRow.addClass("selected");
                editTable.append(tableRow);
                console.log("result json: " + JSON.stringify(dataSeries));

                editedFocusArea = data.focusAreaId;

                if (data.author != undefined) {
                    $('div#author-info').show();
                    // Set header fields
                    var authorNameHtml = jQuery(impactSeriesAuthorTemplate({author: data.author}));
                    $('div#impact-series-detail div#header span#author-name').html(authorNameHtml);
                    $('div#impact-series-detail div#header span#save-date').text(data.updateDate);

                } else {
                    $('div#author-info').hide();
                }

                // Disable save button for unchanged new serieses
                if (currentEditingRowIndex == ROW_INDEX_NONE_SELECTED) {
                    $('a#save-button').parent().addClass('disabled');
                }

                // Register input event handler
                $('#impact table tr.impact-edit-row input').on('blur', function() {
                    // check valid input
                    var floatValue = parseFloat($(this).attr('value'));

                    //  descendant from IMPACT_REDUCTION edit row -> allow negative values
                    if ($(this).parents('#impact-edit-row-IMPACT_REDUCTION').length > 0) {
                        if (floatValue &lt; -100.0) {
                            $(this).attr('value', "-100");
                        }
                    } else {
                        if (floatValue &lt; 0) {
                            $(this).attr('value', "0");
                        }
                    }

                    if (floatValue &gt; 100) {
                        $(this).attr('value', "100");
                    }

                    userInputOccurred = true;
                    $('a#save-button').parent().removeClass('disabled')
                    recalculateEditSeriesValues();
                });

                recalculateEditSeriesValues();
            });
        }

        function registerDropdownEventListener(regionDropdownElement, sectorDropdownElement) {
            sectorDropdownElement.click(function(event) {event.stopPropagation();});
            regionDropdownElement.click(function(event) {event.stopPropagation();});

            regionDropdownElement.on('change', function() {
                $('#impact a#continue-button').parent().addClass('disabled');

                var regionTermId = $(this).val();
                console.log("region term selected: " + regionTermId);
                if (regionTermId == 0) {
                    clearDropdown(sectorDropdownElement, "Select sector");
                    sectorDropdownElement.attr('disabled', 'disabled');
                    return;
                }

                // Get sector terms
                var url = replaceImpactURLPlaceholders(getSectorsForRegionURL, [REGION_TERM_ID_PLACEHOLDER], [regionTermId]);
                setDetailViewSpinnerMode(true);
                $('div#new-series select#sector').attr('disabled', 'disabled');

                $.getJSON(url, { get_param: 'value' }, function(data) {
                    setDetailViewSpinnerMode(false);
                    clearDropdown(sectorDropdownElement, "Select sector");

                    $.each(data, function(index, attr) {
                        sectorDropdownElement.append('<option value="' + attr.id + '">' + attr.name + '</option>');
                    });

                    sectorDropdownElement.removeAttr('disabled');
                    sectorDropdownElement.focus();
                });
            });

            sectorDropdownElement.on('change', function() {
                $('#impact a#continue-button').parent().addClass('disabled');
                var sectorTermId = sectorDropdownElement.val();
                var regionTermId = regionDropdownElement.val();

                if (sectorTermId &gt; 0 &amp;&amp; regionTermId &gt; 0) {
                    $('#impact a#continue-button').parent().removeClass('disabled');
                    $('#impact a#continue-button').focus();
                }
            });
        }

        function clearDropdown(dropdownElement, defaultOptionString) {
            dropdownElement.empty();
            dropdownElement.append('<option value="' + 0 + '">' + defaultOptionString + '</option>');
        }

        function loadRegionTerms() {
            var regionDropdownElement = $('div#new-series select#region');

            var sectorDropdownElement = $('div#new-series select#sector');
            sectorDropdownElement.empty();
            sectorDropdownElement.append('<option value="' + 0 + '">Select sector</option>');

            var url = replaceImpactURLPlaceholders(getRegionsURL, [], []);
            setDetailViewSpinnerMode(true);
            $.getJSON(url, { get_param: 'value' }, function(data) {
                setDetailViewSpinnerMode(false);
                regionDropdownElement.empty();
                regionDropdownElement.append('<option value="' + 0 + '">Select region</option>');
                console.log("data " + data);

                $.each(data, function(index, attr) {
                    console.log("each " + attr);
                    regionDropdownElement.append('<option value="' + attr.id + '">' + attr.name + '</option>');
                });
            });
        }

        function saveDataSeries(sender) {
            if (!userInputOccurred) {
                disableEditMode();
                return;
            }

            var url = replaceImpactURLPlaceholders(saveDataSeriesURL, [FOCUS_AREA_ID_PLACEHOLDER], [editedFocusArea]);

            var reductionData = {};
            var adoptionData = {};

            $.each($('tr#impact-edit-row-' + IMPACT_REDUCTION_PLACEHOLDER+ ' input'), function(idx) {
                reductionData[$(this).attr('name')] = $(this).attr('value');
            });
            $.each($('tr#impact-edit-row-' + IMPACT_ADOPTION_RATE_PLACEHOLDER+ ' input'), function(idx) {
                adoptionData[$(this).attr('name')] = $(this).attr('value');
            });

            var postJson = {};
            postJson[IMPACT_REDUCTION_PLACEHOLDER] = reductionData;
            postJson[IMPACT_ADOPTION_RATE_PLACEHOLDER] = adoptionData;

            setDetailViewSpinnerMode(true);
            $.post(url, {"json" : JSON.stringify(postJson)}, function(response) {
                responseData = JSON.parse(response);
                console.log("responseData " + responseData);
                if (!responseData.success) {
                    alert("Could not process request");
                } else {
                    var resultValues = $('div#impact-series-detail table#edit-table #impact-edit-row-RESULT td span');

                    // Editing a new series
                    if (currentEditingRowIndex == ROW_INDEX_NONE_SELECTED) {
                        insertNewSeries(resultValues);
                    }

                    // Editing an existing series
                    else {
                        console.log("else");
                        updateSeriesValues(resultValues);
                    }

                    disableEditMode();
                    editedFocusArea = 0;
                }

                setDetailViewSpinnerMode(false);
            });
        }

        function newSeriesRowClicked(event) {
            // Disable edit mode if present
            disableEditMode();

            // Toggle new series view
            setShowNewSeries(!newSeriesModeEnabled);
            newSeriesModeEnabled = !newSeriesModeEnabled;

        }

        function continueButtonClicked(event) {
            newRegionTermId = $('div#new-series select#region').val();
            newSectorTermId = $('div#new-series select#sector').val();
            console.log("url " + getDataSeriesURL);
            var url = replaceImpactURLPlaceholders(getDataSeriesURL,
                    [SECTOR_TERM_ID_PLACEHOLDER, REGION_TERM_ID_PLACEHOLDER],
                    [newSectorTermId, newRegionTermId]);

            console.log("load data series with URL " + url);

            showEditTable();
            loadSeriesEditData(url);

            // Replace detail header
            $('div#impact-series-detail #header h3#region-title').text($('div#new-series select#region option:selected').text());
            $('div#impact-series-detail #header h3#sector-title').text($('div#new-series select#sector option:selected').text());
            // userInputOccurred = true;

            $('#delete-button').parent().removeClass('disabled');
        }

        function setShowNewSeries(shouldShow) {
            if (shouldShow) {
                $('div#impact-series-detail').fadeIn();

                $('div#impact-series-detail div#new-series').fadeIn();
                $('div#impact-series-detail div#header').hide();
                $('div#impact-series-detail table#edit-table').fadeOut();

                loadRegionTerms();

                $('tr#impact-new-series-row').addClass('selected');
                $('div#new-series select#region').focus();
                $('div#new-series select#sector').attr('disabled', 'disabled');
            } else {
                $('div#impact-series-detail').fadeOut();
                $('tr#impact-new-series-row').removeClass('selected');

                $('div#impact-series-detail div#new-series').fadeOut();
                $('div#impact-series-detail div#header').hide();
                $('div#impact-series-detail table#edit-table').fadeOut();
            }

        }

        function showEditTable() {
            $('div#impact-series-detail').fadeIn();

            $('div#impact-series-detail div#new-series').hide();
            $('div#impact-series-detail div#header').fadeIn();
            $('div#impact-series-detail table#edit-table').fadeIn();
        }

        function dismissDetailView() {
            $('div#impact-series-detail').fadeOut('normal', function() {
                $(this).find('#edit-table tbody').empty();

            });
            $('div#impact-series-detail div#new-series').hide();
            $('div#impact-series-detail div#header').fadeOut();
            $('div#impact-series-detail table#edit-table').fadeOut();
        }

        function setDetailViewSpinnerMode(on) {
            if (on) {
                $('#impact-series-detail').addClass('disabled');
                $('#impact-series-detail input').attr('disabled', 'disabled');
                $('#impact-series-detail a').parent().addClass('button-disabled');
                $('#impact-summary tr').addClass('disabled');

                $('#impact-series-detail div#header').spin('large');
                $('#impact-series-detail div#new-series').spin('large');
            } else {
                $('#impact-series-detail').removeClass('disabled');
                $('#impact-series-detail input').removeAttr('disabled');
                $('#impact-series-detail a').parent().removeClass('button-disabled');
                $('#impact-summary  tr').removeClass('disabled');

                $('#impact-series-detail div#header').spin(false);
                $('#impact-series-detail div#new-series').spin(false);
            }
        }
        function insertNewSeries(resultValues) {

            var regionTermName = $('div#impact-series-detail h3#region-title').text();
            var sectorTermName = $('div#impact-series-detail h3#sector-title').text();
            var seriesData = {"sectorTerm" : {"id": newSectorTermId, "name" : sectorTermName},
                "regionTerm" : {"id": newRegionTermId, "name" : regionTermName},
                "rowIndex": $('tr.impact-series-clickable').size(),
                "resultValues" : [],
                "focusAreaId" : editedFocusArea};

            //console.log("series data " + JSON.stringify(seriesData));
            $.each(resultValues, function(idx) {
                seriesData.resultValues.push($(this).text());
            });
            // Insert new row
            var newRow = jQuery(impactSeriesNewTableRowTemplate({series: seriesData}));
            var overviewTableRows = $('table#impact-summary tr.impact-series-clickable');
            if (overviewTableRows.length == 0) {
                $('tbody#summary-body').append(newRow);
            } else {
                newRow.insertAfter(overviewTableRows[overviewTableRows.length - 1]);
            }

            newRow.click(function() {
                toggleEditMode($(this));
            });
        }

        function updateSeriesValues(resultValues) {
            for (var i = 0; i &lt; resultValues.length; i++) {
                var valueCell = $($('tr#impact-row-'+currentEditingRowIndex + ' td.impact-value')[i]);
                console.log("get valueCell with index " + i);
                valueCell.text($(resultValues[i]).text());

                console.log("Set value " + valueCell.text() + " for index " + i);
            }
        }
        function deleteSeriesRow(event) {
            if (currentEditingRowIndex != ROW_INDEX_NONE_SELECTED) {
                $('#impact table tr.impact-edit-row').fadeOut('normal', function() {
                    $(this).remove();
                });

                event.stopPropagation();
                console.log('focus area id ' + editedFocusArea);
                var url = replaceImpactURLPlaceholders(deleteDataSeriesURL, [FOCUS_AREA_ID_PLACEHOLDER], [editedFocusArea]);
                console.log("delete url " + url);
                var row = getSelectedOverviewTableRow();
                console.log("row: " + row.html());

                setDetailViewSpinnerMode(true);
                $.post(url, {}, function(response) {
                    var responseData = JSON.parse(response);
                    setDetailViewSpinnerMode(false);
                    if (!responseData.success) {
                        alert("Could not delete row");
                    } else {
                        row.fadeOut("normal", function(){ row.remove(); });
                        dismissDetailView();

                        // Reload available sector terms
                        var regionsSelectElement = $('div#impact-series-detail div#new-series select#region');
                        regionsSelectElement.empty();
                        regionsSelectElement.append('<option value="0" selected="selected">Select region</option>');
                        $.getJSON(getRegionsURL, { get_param: 'value' }, function(data) {
                            console.log("get regions data " + data);
                            if (data != null) {
                                $.each(data, function(index, attr) {
                                    regionsSelectElement.append('<option value="' + attr.id + '">' + attr.name + '</option>');
                                });
                            }
                        });
                    }
                });

                currentEditingRowIndex = ROW_INDEX_NONE_SELECTED;
            } else {
                dismissDetailView();
                $('tr#impact-new-series-row').removeClass('selected');
            }
        }

        function getOverviewTableRowWithIndex(index) {
            console.log('tr#impact-row-' + index);
            return $('tr#impact-row-' + index);
        }

        function getIndexFromTableRow(row) {
            return row.attr('id').substring("impact-row-".length);
        }

        function getSelectedOverviewTableRow() {
            return getOverviewTableRowWithIndex(currentEditingRowIndex);
        }

        var impactSeriesEditTableRowTemplate = Handlebars.compile($("#impactSeriesEditTableRowTemplate").html());
        var impactSeriesSaveButtonTemplate = Handlebars.compile($("#impactSeriesSaveButtonTemplate").html());
        var impactSeriesNewTableRowTemplate = Handlebars.compile($('#impactSeriesNewTableRowTemplate').html());
        var impactSeriesAuthorTemplate = Handlebars.compile($('#impactSeriesAuthorTemplate').html());
    </script>
</jsp:root>