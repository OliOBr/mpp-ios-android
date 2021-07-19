import UIKit
import SharedCode

class ViewController: UIViewController,  UITableViewDelegate, UITableViewDataSource,UITextFieldDelegate, ApplicationContractMainView {

    @IBOutlet var originStationSelector: UITextField!
    @IBOutlet var destinationStationSelector: UITextField!
    @IBOutlet private var button: UIButton!
    @IBOutlet var tableView: UITableView!
    var senderID: String = ""
    let cellReuseIdentifier = "JourneyCellType"
    var journeys: [Journey] = []
    
    var originStationCRS = ""
    var destStationCRS = ""

    private let presenter: ApplicationContractPresenter = ApplicationPresenter()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.delegate = self
        tableView.dataSource = self
    }
    
    //TODO: Breaks if empty or if same station or if no routes between station
    @IBAction func onClickButton() {
        presenter.getAndDisplayJourneysData(view: self, arrivalStation: originStationCRS, departureStation: destStationCRS)
    }
    
    // number of rows in table view
     func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
         return self.journeys.count
     }
     
     // create a cell for each table view row
     func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
         
         // create a new cell if needed or reuse an old one
        let cell = tableView.dequeueReusableCell(withIdentifier: cellReuseIdentifier, for: indexPath ) as! JourneyViewCell
         
         // set the text from the data model
        
        cell.arrivalTimeText.text = self.journeys[indexPath.row].arrivalTime
        cell.departureTimeText.text = self.journeys[indexPath.row].departureTime
        cell.destinationStationText.text =  self.journeys[indexPath.row].destinationStation
        cell.originStationText.text = self.journeys[indexPath.row].originStation
        cell.statusText.text = self.journeys[indexPath.row].status
        return cell
     }
    
    @objc(displayJourneysInRecyclerViewJourneysData:) func displayJourneysInRecyclerView(journeysData: [Journey]) {
        journeys = journeysData
        tableView.reloadData()
    }

    @IBAction func editingChangedDestinationText(_ textField: UITextField) {
        senderID = "fromText"
        performSegue(withIdentifier: "SegueToSearch", sender: self)
    }
    @IBAction func onTouchDestinationText(_ sender: Any) {
        senderID = "fromText"
        performSegue(withIdentifier: "SegueToSearch", sender: self)
    }
    
    @IBAction func editingChangesArrivalText(_ sender: Any) {
        senderID = "toText"
        performSegue(withIdentifier: "SegueToSearch", sender: self)
    }
    
    @IBAction func onTouchArrivalText(_ sender: Any) {
        senderID = "toText"
        performSegue(withIdentifier: "SegueToSearch", sender: self)
    }
    
    // segue ViewControllerB -> ViewController
    @IBAction func unwind( _ sender: UIStoryboardSegue) {
        if let sourceViewController = sender.source as? SearchController {
            if (senderID == "fromText") {
                originStationSelector.text = sourceViewController.stationSelected!.stationName
                originStationCRS = sourceViewController.stationSelected!.crs
            } else {
                destinationStationSelector.text = sourceViewController.stationSelected!.stationName
                destStationCRS = sourceViewController.stationSelected!.crs
            }
        }
    }
}

