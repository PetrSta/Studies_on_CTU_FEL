using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using SemesterWork___car_data_database.Models;

namespace SemesterWork___car_data_database
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        FuelRecordsViewModel _fuelRecordsViewModel = new FuelRecordsViewModel();
        ServiceRecordsViewModel _serviceRecordsViewModel = new ServiceRecordsViewModel();
        private int? _selectedFuelDataIndex;
        private int? _selectedServicelDataIndex;
        private FuelRecordsDataModel _selectedFuelData;
        private ServiceRecordsDataModel _selectedServicelData;

        public ObservableCollection<FuelRecordsDataModel> FuelRefuelList { get; set; } = new ObservableCollection<FuelRecordsDataModel>();
        public ObservableCollection<ServiceRecordsDataModel> ServiceList { get; set; } = new ObservableCollection<ServiceRecordsDataModel>();

        public MainWindow()
        {
            InitializeComponent();
        }

        private void FuelConsumtionButtonClick(object sender, RoutedEventArgs e)
        {
            // open fuelConsumptionRecords window
            var fuelList = ((MainWindowViewModel)(this.DataContext)).FuelRefuelList;
            var fuelConsumtionWindow = new FuelConsumtionRecords((MainWindowViewModel)this.DataContext, fuelList);
            fuelConsumtionWindow.ShowDialog();
        }

        private void SeviceButtonClick(object sender, RoutedEventArgs e)
        {
            // open serviceRecords window
            var serviceList = ((MainWindowViewModel)(this.DataContext)).ServiceList;
            var serviceWindow = new ServiceRecords((MainWindowViewModel)this.DataContext, serviceList);
            serviceWindow.ShowDialog();
        }
    }
}
