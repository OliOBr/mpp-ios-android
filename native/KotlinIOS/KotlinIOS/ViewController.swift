import UIKit
import SharedCode

class ViewController: UIViewController, UIPickerViewDataSource, UIPickerViewDelegate,  UITableViewDelegate, UITableViewDataSource,UITextFieldDelegate {
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {return pickerData.count}

    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return pickerData[row]
    }

    @IBOutlet var originStationSelector: UITextField!
    @IBOutlet var destinationStationSelector: UITextField!
    @IBOutlet private var button: UIButton!
    @IBOutlet var tableView: UITableView!
    var senderID: String = ""
    var pickerData: [String] = [String]()
    let cellReuseIdentifier = "JourneyCellType"
    var trains: [Train] = []

    private let presenter: ApplicationContractPresenter = ApplicationPresenter()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.onViewTaken(view: self)
        tableView.delegate = self
        tableView.dataSource = self
        pickerData = ["Newton Abbot","Waterloo","Durham","Cambridge", "Paddington"]
    }
    
    @IBAction func onClickButton() {
        let destinationStation = destinationStationSelector.text!
        let originStation = originStationSelector.text!
        let url = presenter.getAPIURLWithSelectedStationsPresenter(arrivalStation: destinationStation,departureStation: originStation)
        presenter.getData(view: self, url: url)
    }
    
    // number of rows in table view
     func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
         return self.trains.count
     }
     
     // create a cell for each table view row
     func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
         
         // create a new cell if needed or reuse an old one
        let cell = tableView.dequeueReusableCell(withIdentifier: cellReuseIdentifier, for: indexPath ) as! JourneyViewCell
         
         // set the text from the data model
        
        cell.arrivalTimeText.text = self.trains[indexPath.row].arrivalTime
        cell.departureTimeText.text = self.trains[indexPath.row].departureTime
        cell.destinationStationText.text =  self.trains[indexPath.row].destinationStation
        cell.originStationText.text = self.trains[indexPath.row].originStation
        cell.statusText.text = self.trains[indexPath.row].status
        return cell
     }
    
    func updateTrainsRecycleView(newTrains: [Train]) {
        trains = newTrains
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
            if(senderID == "fromText") {
            originStationSelector.text = sourceViewController.stationSelected
            } else {
                destinationStationSelector.text = sourceViewController.stationSelected
            }
        }
    }
}

extension ViewController: ApplicationContractView {
    
    func setLabel(text: String) {
        
    }
}
