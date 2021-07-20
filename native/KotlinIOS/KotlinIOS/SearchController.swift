import UIKit
import SharedCode
import CoreLocation

class SearchController: UIViewController,UITableViewDelegate,UITableViewDataSource, SearchStationsContractView {
    
    @IBOutlet var searchBar: UISearchBar!
    @IBOutlet var tableView: UITableView!
    var searching = false
    var senderID: String = ""
    
    var stationSelected : Station?
    var searchedStations: [Station] = []
    var stations: [Station] = []
    var locationManager = CLLocationManager()
    
    var currentLoc: CLLocation!
    
    var searchDelegate:SearchDelegate?
    
    private let presenter: SearchStationsContractPresenter = SearchStationsPresenter()
    
    func listStationsInListView(stationData: [Station]) {
        addDistancesToStations(stationData: stationData)
        stations = stationData.sorted(by:{($0.distanceFromLocation ?? 100000).doubleValue  < ($1.distanceFromLocation ?? 100000).doubleValue})
        print(stations[0].distanceFromLocation?.stringValue)
        tableView.reloadData()
    }
    
    func addDistancesToStations(stationData: [Station]) {
        for station in stationData {
            if(currentLoc != nil) {
                let distance =  station.getDistanceFromLocation(locationLongitude: currentLoc.coordinate.longitude,locationLatitude: currentLoc.coordinate.latitude)
                if(distance != nil){
                    station.distanceFromLocation = distance
                }
            }
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.delegate = self
        tableView.dataSource = self
        locationManager.requestWhenInUseAuthorization()
        if(CLLocationManager.authorizationStatus() == .authorizedWhenInUse ||
        CLLocationManager.authorizationStatus() == .authorizedAlways) {
           currentLoc = locationManager.location
        }
        presenter.getAndListStationsData(view: self)
    }
    
    
    // number of rows in table view
     func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if (searching) {
            return searchedStations.count
        } else {
            return stations.count
        }
     }
     
    // create a cell for each table view row
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {

        // create a new cell if needed or reuse an old one
        let cell = tableView.dequeueReusableCell(withIdentifier: "listItem", for: indexPath ) as! SearchViewCell
        if (searching) {
            cell.stationName.text = searchedStations[indexPath.row].stationName
            if(searchedStations[indexPath.row].distanceFromLocation != nil) {
                cell.distance.text = searchedStations[indexPath.row].distanceFromLocation!.stringValue + " miles"
                
            }
        } else {
            cell.stationName!.text = stations[indexPath.row].stationName
            if(stations[indexPath.row].distanceFromLocation != nil) {
                cell.distance.text = stations[indexPath.row].distanceFromLocation!.stringValue + " miles"
                
            }
        }
        // set the text from the data model
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if (searching) {
            stationSelected = searchedStations[indexPath.row]
        } else {
            stationSelected = stations[indexPath.row]
        }
        performSegue(withIdentifier: "returnSearch", sender: self)
    }
    
    
}

extension SearchController: UISearchBarDelegate {
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        searchedStations = stations.filter { $0.stationName.lowercased().prefix(searchText.count) == searchText.lowercased() }
                searching = true
                tableView.reloadData()
    }
    
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {

    }
}

protocol SearchDelegate {
    func searchResult(result: String)
}
