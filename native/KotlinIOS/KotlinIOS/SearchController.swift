import UIKit
import SharedCode

class SearchController: UIViewController,UITableViewDelegate,UITableViewDataSource, SearchStationsContractView {
    
    @IBOutlet var searchBar: UISearchBar!
    @IBOutlet var tableView: UITableView!
    var searching = false
    var senderID: String = ""
    
    var stationSelected : Station?
    var searchedStations: [Station] = []
    var stations: [Station] = []
    
    var searchDelegate:SearchDelegate?
    
    private let presenter: SearchStationsContractPresenter = SearchStationsPresenter()
    
    func listStationsInListView(stationData: [Station]) {
        stations = stationData
        tableView.reloadData()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.delegate = self
        tableView.dataSource = self
            
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
        let cell = tableView.dequeueReusableCell(withIdentifier: "listItem", for: indexPath )
        if (searching) {
            cell.textLabel!.text = searchedStations[indexPath.row].stationName
        } else {
            cell.textLabel!.text = stations[indexPath.row].stationName
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
