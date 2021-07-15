import UIKit
import SharedCode

class SearchController: UIViewController,UITableViewDelegate,UITableViewDataSource{
    @IBOutlet var searchBar: UISearchBar!
    @IBOutlet var tableView: UITableView!
    var searching = false
    var senderID: String = ""
    var stationSelected : String?
    var searchedStations: [String] = []
    var stations: [String] = ["Newton Abbot","Waterloo","Durham","Cambridge","Paddington"]
    var searchDelegate:SearchDelegate?
    override func viewDidLoad() {
            super.viewDidLoad()
            tableView.delegate = self
            tableView.dataSource = self
            // Uncomment the following line to preserve selection between presentations
            // self.clearsSelectionOnViewWillAppear = false

            // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
            // self.navigationItem.rightBarButtonItem = self.editButtonItem
        }
    // number of rows in table view
     func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if(searching){
            return searchedStations.count
        }
        else{
         return stations.count
        }
     }
     
     // create a cell for each table view row
     func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
         
         // create a new cell if needed or reuse an old one
        let cell = tableView.dequeueReusableCell(withIdentifier: "listItem", for: indexPath )
        if(searching){
            cell.textLabel!.text = searchedStations[indexPath.row]
        }
        else{
            cell.textLabel!.text = stations[indexPath.row]
        }
         // set the text from the data model
        return cell
     }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if(searching){
        stationSelected = searchedStations[indexPath.row]
        }else{
            stationSelected = stations[indexPath.row]
        }
        performSegue(withIdentifier: "returnSearch", sender: self)
        }
    
    
}

extension SearchController: UISearchBarDelegate {
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        searchedStations = stations.filter { $0.lowercased().prefix(searchText.count) == searchText.lowercased() }
                searching = true
                tableView.reloadData()
    }
    
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {

    }
}

protocol SearchDelegate {
    func searchResult(result: String)
}
