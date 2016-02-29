var stockMonitor = {};
var delSymbol;
$(document).ready(function () {
    stockMonitor.getTemplate = function (field) {
        var row = '<tr>' + '<td class="symbol">' + field.symbol + '</td>' + '<td>' + field.name + '</td>' + '<td>' + field.price + '</td>' + '<td>' + field.changeValue + '</td>' + '<td>' + field.changePercent + '</td>' +
            '<td>' + field.volume + '</td>' + '<td>' + field.prevClose + '</td>' + '<td>' + field.open + '</td>' + '<td>' + field.dayHigh + '</td>' + '<td>' + field.dayLow + '</td>' + '<td>' + '<button type="button" class="btn btn-danger delete">DELETE' + '</td>' + '<td>' + '<button type="button" class="btn btn-info history">HISTORY' + '</td>' + '</tr>';
        return row;
    }
    $.ajax({
        url: '/StockMonitor/api/stocks',
        type: 'GET',
        dataType: 'json',
        beforeSend:function(){
            $('#loading').html("Loading ...");
        },
        success: function (data) {
            if (data.length > 0) {
                $.each(data, function (i, field) {
                    $("tbody").append(stockMonitor.getTemplate(field));
                });
                $('#loading').empty();
            }
            else {
                alert("No data found!");
                $('#loading').empty();
            }
        },
        error: function () {
            alert('Error! Please try again.');
        }
    });
    initializeEvents();
});

function initializeEvents(){
    $("table").on("click", ".delete", function (e) {
        e.preventDefault();
        stockMonitor.delSymbol=$(this).closest('tr').find('.symbol').text();
        $.ajax({
            type: "DELETE",
            url: '/StockMonitor/api/stocks/'+stockMonitor.delSymbol,
            dataType: 'json',
            success: function () {
                location.reload();
            },
            done: function () {
                location.reload();
            },
            error: function (jqXHR,textStatus,erroThrown) {
            	location.reload();
            }
        });
    });
    $("table").on("click", ".history", function (e) {
        e.preventDefault();
        stockMonitor.historySymbol = $(this).closest('tr').find('.symbol').text();
        $.ajax({
            type: "GET",
            url: '/StockMonitor/api/stocks/' + stockMonitor.historySymbol,
            dataType: 'json',
            success: function (data) {
                var historyData=[];
                if (data.length > 0) {
                    $.each(data, function (i, field) {
                        var historyObject={};
                        historyObject.date=field.date;
                        historyObject.open=field.open;
                        historyData.push(historyObject);
                    });
                    console.log(historyData);
                }
                InitChart(historyData);
                window.scrollTo(0,1000);
            }
        });
    });
    $('.add').on('click', function (event) {
        event.preventDefault();
        stockMonitor.symbol=$('#enteredSymbol').val();
        $.ajax({
            type: "POST",
            url: '/StockMonitor/api/stocks',
            contentType: "application/json; charset=utf-8",
        	data: stockMonitor.symbol,
            success: function () {
                console.log("Added a new symbol");
                location.reload();
            },
            error: function(jqXHR, textStatus, errorThrown) {
            	
            	if(jqXHR.status == 404)
            	{
            		alert("There is no stock associated with this Symbol");
            	}
            	else if(jqXHR.status == 400)
            	{
            		alert("The Stock already exists in the system");
            	}
            }
        })
    });
};

function InitChart(historyData) {
//JSON data for graph
    
	//d3.select("#visualisation").remove();
	d3.selectAll("svg > *").remove();
	var data = historyData;
	//initializing dimensions of the visualisation
        var vis = d3.select("#visualisation").append('svg'),
            WIDTH = 1000,
            HEIGHT = 600,
            PADDING = 20,
            MARGINS = {
                top: 20,
                right: 20,
                bottom: 20,
                left: 50
        }
        
        vis.attr('height', HEIGHT)
           .attr('width', WIDTH);

        //Defining time format
        var timeFormat = d3.time.format('%Y-%m-%d');
        
        //Defining range for x. Defining range and domain for y
        var x = d3.time.scale().range([MARGINS.left, WIDTH - MARGINS.right])
        var y = d3.scale.linear().range([HEIGHT - MARGINS.top, MARGINS.bottom])//.domain([0, 20])


        //Defining domain for x
        x.domain([timeFormat.parse(data[data.length-1].date), timeFormat.parse(data[0].date)])
        //x.domain(d3.extent(data, function (d) { return d.metricDate; }));
        y.domain([d3.min(data, function (d) { return +d.open; }), d3.max(data, function (d) { return +d.open; })]);


        //Define x axis
        var xAxis = d3.svg.axis()
            .scale(x)
            .tickSize(5)
            .orient("bottom")
            .tickFormat(d3.time.format("%b-%y")); //<== insert the tickFormat function

        //Define y axis
        var yAxis = d3.svg.axis()
            .scale(y)
            .tickSize(5)
            .orient("left")
            .tickSubdivide(true);
        
     
        
        //Appending the axes to the svg
        vis.append("svg:g")
            .attr("class", "axis")
            .attr("transform", "translate(0," + (HEIGHT - MARGINS.bottom) + ")")
            .call(xAxis);

        vis.append("svg:g")
            .attr("class", "axis")
            .attr("transform", "translate(" + (MARGINS.left) + ",0)")
            .call(yAxis);
        
        //Define line
        var lineGen = d3.svg.line()
            .x(function (d) {
                return x(timeFormat.parse(d.date));
            })
            .y(function (d) {
                return y(d.open);
            });
        
     // now add titles to the axes
        vis.append("text")
        	.attr("class","value")
        	.attr("text-anchor", "middle")  // this makes it easy to centre the text as the transform is applied to the anchor
            .attr("transform", "translate("+ (PADDING/2) +","+(HEIGHT/2)+")rotate(-90)")  // text is drawn off the screen top left, move down and out and rotate
            .text("Open Value");
        
        //Appending the line to the svg
        vis.append('svg:path')
            .attr('d', lineGen(data))
            .attr('stroke', 'green')
            .attr('stroke-width', 2)
            .attr('fill', 'none');
        
}




