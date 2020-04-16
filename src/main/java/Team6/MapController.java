package Team6;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import entities.Album;
import entities.Picture;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MapController implements MapComponentInitializedListener, Initializable {
    @FXML
    AnchorPane anchorPane;
    @FXML
    GoogleMapView mapView;
    @FXML
    GoogleMap map;

    List<Picture> pictures = Context.getInstance().currentAlbum().getPictures();

    //InfoBilde over markers

    @Override
    public void mapInitialized() {
        //Set the initial properties of the map.
        MapOptions mapOptions = new MapOptions();

        mapOptions.center(new LatLong(63.446827, 10.421906))
                .mapType(MapTypeIdEnum.ROADMAP)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(true)
                .zoom(12);

        map = mapView.createMap(mapOptions);
        map.addMarkers(createMarkers());
    }

    List<Marker> createMarkers () {
        return pictures.stream().map(x -> {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLong(x.getLatitude(), x.getLongitude()))
                    .visible(true);
            return new Marker(markerOptions);
        }).collect(Collectors.toList());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mapView.addMapInializedListener(this);
    }
}
