var COLUMNS_TO_CHECK=1000;

/**
 * Adds a custom menu with items to show the sidebar and dialog.
 *
 * @param {Object} e The event parameter for a simple onOpen trigger.
 */
function onOpen(e) {
  SpreadsheetApp.getUi()
      .createAddonMenu()
      .addItem('Show responses for active row', 'showResponses')
      .addToUi();
}

/**
 * Runs when the add-on is installed; calls onOpen() to ensure menu creation and
 * any other initializion work is done immediately.
 *
 * @param {Object} e The event parameter for a simple onInstall trigger.
 */
function onInstall(e) {
  onOpen(e);
}

function showResponses() {
  var ss = SpreadsheetApp.getActiveSheet();
  var activeRow = ss.getActiveCell().getRow();

  var values = getGroupedData(activeRow);
  var outputHTML = '';
  values.forEach(function(data) {
    outputHTML += '<h4>'+ data.question + '</h4>' +
                  '<p>' + data.response + '</p>';
  });
  var ui = HtmlService.createHtmlOutput(outputHTML)
      .setWidth(600)
      .setHeight(500);

  SpreadsheetApp.getUi().showModalDialog(ui, 'View Responses for row ' + activeRow);
}

function getGroupedData(row) {
  var header = getRow(1);
  var data = getRow(row);

  var grouped = [];
  for (var i = 0; i < header.length; i++) {
    if (header[i] && data[i]) {
      grouped.push({
        question: header[i],
        response: formatQuestionResponse(data[i])
      });
    }
  }

  return grouped;
}

function getRow(row) {
  var ss = SpreadsheetApp.getActiveSheet();

  return ss.getRange(row,1,1,COLUMNS_TO_CHECK).getValues()[0];
}

/**
* Make sure response data is formatted properly
*/
function formatQuestionResponse(response) {
  return response.toString().replace(/\n/g, '<br>');
}
