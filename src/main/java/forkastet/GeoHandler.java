package forkastet;

import entities.Picture;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;

public class GeoHandler {

    private PrintStream ps = new PrintStream(new File("tagMapOutPut.html"));

    public GeoHandler() throws FileNotFoundException {
    }

    public void createMap(ArrayList<Picture> list){
        ps.append("<!DOCTYPE html>\n");
        ps.append("<html>\n");
        ps.append("<head>\n");
        ps.append("<meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\" />\n");
        ps.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>\n");
        ps.append("<style>html,body{height:100%;margin:0;padding:0;}#map_canvas{height:100%;}</style>\n");
        ps.append("<script type=\"text/javascript\" src=\"https://maps.googleapis.com/maps/api/js?sensor=false\"></script>\n");
        ps.append("<script type=\"text/javascript\">\n");
        ps.append("function initialise() {\n");
        ps.append("    var options = { zoom:2, mapTypeId:google.maps.MapTypeId.ROADMAP, center:new google.maps.LatLng(0.0, 0.0)};\n");
        ps.append("    var map = new google.maps.Map(document.getElementById('map_canvas'), options);\n");
        ps.append("    var marker;\n");

        for (Picture pic : list)
        {
            final String fullPath = pic.getFilepath();

            ps.append("    marker = new google.maps.Marker({\n");
            ps.append("        position:new google.maps.LatLng(").append(String.valueOf(pic.getLatitude())).append(", ").append(String.valueOf(pic.getLongitude())).append("),\n");
            ps.append("        map:map,\n");
            ps.append("        title:\"").append(fullPath).append("\"});\n");
            ps.append("    google.maps.event.addListener(marker, 'click', function() { document.location = \"").append(fullPath).append("\"; });\n");
        }

        ps.append("}\n");
        ps.append("</script>\n");
        ps.append("</head>\n");
        ps.append("<body onload=\"initialise()\">\n");
        ps.append("<div id=\"map_canvas\"></div>\n");
        ps.append("<script async defer\n" +
                "    src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyBBGsAPf7r7a5jTWm3O_VFERTAZJKX3e2k&callback=initMap\">\n" +
                "    </script>");
        ps.append("</body>\n");
        ps.append("</html>\n");
    }


}
