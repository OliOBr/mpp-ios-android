import UIKit
import SharedCode

class ViewController: UIViewController, UIPickerViewDataSource, UIPickerViewDelegate,  UITableViewDelegate, UITableViewDataSource {
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {return pickerData.count}

    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return pickerData[row]
    }

    @IBOutlet private var arrivalStationPicker: UIPickerView!
    @IBOutlet private var departureStationPicker: UIPickerView!
    @IBOutlet private var button: UIButton!
    @IBOutlet var tableView: UITableView!
    var pickerData: [String] = [String]()
    let cellReuseIdentifier = "JourneyCellType"
    var trains: [Train] = []

    private let presenter: ApplicationContractPresenter = ApplicationPresenter()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.onViewTaken(view: self)
        arrivalStationPicker.delegate = self
        arrivalStationPicker.dataSource = self
        departureStationPicker.delegate = self
        departureStationPicker.dataSource = self
        tableView.delegate = self
        tableView.dataSource = self
        pickerData = ["Newton Abbot","Waterloo","Durham","Cambridge", "Paddington"]
    }
    
    @IBAction func onClickButton() {
        let arrivalStation = pickerData[arrivalStationPicker.selectedRow(inComponent: 0)]
        let departureStation = pickerData[departureStationPicker.selectedRow(inComponent: 0)]
        let url = presenter.getAPIURLWithSelectedStationsPresenter(arrivalStation: arrivalStation,departureStation: departureStation)
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
         
        return cell
     }
    
    func updateTrainsRecycleView(newTrains: [Train]) {
        trains = trains + newTrains
        tableView.reloadData()
    }
}

extension ViewController: ApplicationContractView {
    
    func setLabel(text: String) {
        
    }
}
