import UIKit
import SharedCode

class ViewController: UIViewController,  UITableViewDelegate, UITableViewDataSource,UITextFieldDelegate, MainContractView {

    @IBOutlet var originStationSelector: UITextField!
    @IBOutlet var destinationStationSelector: UITextField!
    @IBOutlet private var button: UIButton!
    @IBOutlet var tableView: UITableView!
    @IBOutlet var label: UILabel!
    @IBOutlet var progressLoader: UIProgressView!
    
    var senderID: String = ""
    let cellReuseIdentifier = "JourneyCellType"
    var journeys: [Journey] = []
    
    var originStationCRS = ""
    var destStationCRS = ""

    private let presenter: MainContractPresenter = MainPresenter()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.delegate = self
        tableView.dataSource = self
    }
    
    @IBAction func onClickButton() {
        tableView.isHidden = true
        label.isHidden = true
        progressLoader.isHidden = false
        presenter.getAndDisplayJourneysData(view: self, originStationCRS: originStationCRS, destStationCRS: destStationCRS)
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
         cell.ticketPrice.text = self.journeys[indexPath.row].ticketPriceInPounds
        return cell
     }
    
    @objc(displayJourneysInRecyclerViewJourneysData:) func displayJourneysInRecyclerView(journeysData: [Journey]) {
        progressLoader.isHidden = true
        if (journeysData.isEmpty) {
            label.isHidden = false
            tableView.isHidden = true
            return
        }
        journeys = journeysData
        tableView.isHidden = false
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

