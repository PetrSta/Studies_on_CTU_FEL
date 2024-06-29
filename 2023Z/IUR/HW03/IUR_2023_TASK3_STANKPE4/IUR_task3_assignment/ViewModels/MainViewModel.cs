using IUR_P07_solved.Support;
using IUR_P11_solved.ValidationRules;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Controls;
using System.Windows.Input;
using WeatherInfoCustomControl.Support;

namespace IUR_P07_solved.ViewModels
{
    public class MainViewModel : ViewModelBase
    {
        public ObservableCollection<WeatherCardViewModel> WeatherCards { get; set; } = new ObservableCollection<WeatherCardViewModel>();
        private RelayCommand _addCityCommand;
        private string _cityToBeAdded = "";
        private bool _validationResult;

        public bool ValidationResult
        {
            get => _validationResult;
            set => SetProperty(ref _validationResult, value);
        }

        public RelayCommand AddCityCommand
        {
            get { return _addCityCommand ?? (_addCityCommand = new RelayCommand(AddCity, AddCityCanExecute)); }
        }

        private void AddCity(object obj)
        {
            WeatherCards.Add(new WeatherCardViewModel(this, CityToBeAdded));
            CityToBeAdded = "";
        }

        // TODO EDIT, either CityToBeAdded needs to get updated even if validation rule fails or
        // _validationResult needs to be binded
        private bool AddCityCanExecute(object obj)
        {
            bool cityCanBeAdded = false;

            if (CityToBeAdded.Length >= 2)
            {
                cityCanBeAdded = true;
            }

            return cityCanBeAdded;
        }

        public string CityToBeAdded
        {
            get => _cityToBeAdded;
            set => SetProperty(ref _cityToBeAdded, value);
        }

        public MainViewModel()
        {
            WeatherNet.Util.Api.ApiClient.ProvideApiKey("");

            WeatherCards.Add(new WeatherCardViewModel(this, "Praha"));
            WeatherCards.Add(new WeatherCardViewModel(this, "Brno"));
            WeatherCards.Add(new WeatherCardViewModel(this, "Ostrava"));
            WeatherCards.Add(new WeatherCardViewModel(this, "Jihlava"));
            WeatherCards.Add(new WeatherCardViewModel(this, "Rakovník"));

        }
    }
}
