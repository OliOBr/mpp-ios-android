import UIKit
import SharedCode

class ViewController: UIViewController, UIPickerViewDataSource, UIPickerViewDelegate {
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {return pickerData.count}

    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return pickerData[row]
        
    }

    @IBOutlet private var label: UILabel!
    @IBOutlet private var arrivalStationPicker: UIPickerView!
    @IBOutlet private var departureStationPicker: UIPickerView!
    @IBOutlet private var button: UIButton!
    var pickerData: [String] = [String]()

    private let presenter: ApplicationContractPresenter = ApplicationPresenter()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.onViewTaken(view: self)
        arrivalStationPicker.delegate = self
        arrivalStationPicker.dataSource = self
        departureStationPicker.delegate = self
        departureStationPicker.dataSource = self
        pickerData = ["Newton Abbot","Waterloo","Durham","Cambridge", "Paddington"]
    }
    
    @IBAction func onClickButton() {
        var arrivalStation = pickerData[arrivalStationPicker.selectedRow(inComponent: 0)]
        var departureStation = pickerData[departureStationPicker.selectedRow(inComponent: 0)]
        presenter.
        if let url = URL(string: presenter.getAPIURLWithSelectedStationsPresenter(arrivalStation,departureStation)) {
            UIApplication.shared.open(url)
        }
    }

}

extension ViewController: ApplicationContractView {
    func setLabel(text: String) {
        label.text = text
    }
}
