using System;
using System.Collections.Generic;
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

namespace IUR_TASK1
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {

        // function that checks for values that are not in open weather map before first open
        private void ClearComboBox()
        {
            List<ComboBoxItem> itemsToRemove = new List<ComboBoxItem>();

            foreach (ComboBoxItem item in MainWindowCombo.Items)
            {
                string city = item.Content as string;
                var data = WeatherNet.Current.GetByCityName(city, "Czechia", "en", "metric");
                if (data.Item == null)
                {
                    itemsToRemove.Add(item);
                }
            }

            foreach (ComboBoxItem item in itemsToRemove)
            {
                MainWindowCombo.Items.Remove(item);
            }
        }

        // function to set up 3rd tab header to be date in two days
        private void SetUpHeader()
        {
            DateTime today = DateTime.Today;
            DateTime dayAfterTomorrow = today.AddDays(2);
            string dayInWords = dayAfterTomorrow.DayOfWeek.ToString();
            string dayInNumbers = dayAfterTomorrow.Day.ToString();
            string monthInNumbers = dayAfterTomorrow.Month.ToString();
            string year = dayAfterTomorrow.Year.ToString();
            DayAfterTomorrow.Header = String.Format("{0} {1}.{2} {3}", dayInWords, dayInNumbers, monthInNumbers, year);
        }

        // constructor
        public MainWindow()
        {
            WeatherNet.Util.Api.ApiClient.ProvideApiKey("");
            this.ResizeMode = ResizeMode.NoResize;
            InitializeComponent();
            ClearComboBox();
            SetUpHeader();
        }

        // function to update data after combo box selection changes
        private void ComboBoxSelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if(e.AddedItems.Count > 0)
            {
                string cityName = (e.AddedItems[0] as ComboBoxItem).Content.ToString();
                var currentData = WeatherNet.Current.GetByCityName(cityName, "Czechia", "en", "metric");
                var forecast = WeatherNet.Forecast.GetByCityName(cityName, "Czechia", "en", "metric");
                var tomorrowData = forecast.Items[8];
                var dayAfterTomorrowData = forecast.Items[16];

                // set content of labels to show correct data
                CurrentTemperature.Content = String.Format("{0} °C", currentData.Item.Temp.ToString());
                CurrentHumidity.Content = String.Format("{0} %", currentData.Item.Humidity.ToString());
                CurrentTime.Content = String.Format("{0}", currentData.Item.Date.ToShortTimeString());
                CurrentWeather.Content = currentData.Item.Description.ToString();

                TomorrowTemperature.Content = String.Format("{0} °C", tomorrowData.Temp.ToString());
                TomorrowHumidity.Content = String.Format("{0} %", tomorrowData.Humidity.ToString());
                TomorrowTime.Content = String.Format("{0}", tomorrowData.Date.ToShortTimeString());
                TomorrowWeather.Content = tomorrowData.Description.ToString();

                DayAfterTomorrowTemperature.Content = String.Format("{0} °C", dayAfterTomorrowData.Temp.ToString());
                DayAfterTomorrowHumidity.Content = String.Format("{0} %", dayAfterTomorrowData.Humidity.ToString());
                DayAfterTomorrowTime.Content = String.Format("{0}", dayAfterTomorrowData.Date.ToShortTimeString());
                DayAfterTomorrowWeather.Content = dayAfterTomorrowData.Description.ToString();

            }
        }

        // function which controls logic behind shown combo box item in main window
        private void SetComboItem(ComboBoxItem selectedItem)
        {
            bool itemFound = false;
            
            // if last selected item is found set it as current item to show -> index may change
            if(selectedItem != null)
            {
                foreach (ComboBoxItem comboItem in MainWindowCombo.Items)
                {
                    if (comboItem.Content.ToString() == selectedItem.Content.ToString())
                    {
                        MainWindowCombo.SelectedItem = comboItem;
                        itemFound = true;
                    }
                }
            }
            
            
            // if we didnt find last selected item we will set combo box to be empty and set all labels which show data to be empty
            if(!itemFound)
            {
                MainWindowCombo.SelectedIndex = -1;

                CurrentTemperature.Content = string.Empty;
                CurrentHumidity.Content = string.Empty;
                CurrentTime.Content = string.Empty;
                CurrentWeather.Content = string.Empty;

                TomorrowTemperature.Content = string.Empty;
                TomorrowHumidity.Content = string.Empty;
                TomorrowTime.Content = string.Empty;
                TomorrowWeather.Content = string.Empty;

                DayAfterTomorrowTemperature.Content = string.Empty;
                DayAfterTomorrowHumidity.Content = string.Empty;
                DayAfterTomorrowTime.Content = string.Empty;
                DayAfterTomorrowWeather.Content = string.Empty;
            }
        }

        // function which opens window that is used to manage list of cities
        private void ManageCitiesButtonClick(object sender, RoutedEventArgs e)
        {
            var manageCities = new ManageCities(this);
            manageCities.ResizeMode = ResizeMode.NoResize;
            manageCities.Left = this.Left + this.Width;
            manageCities.Top = this.Top;

            ComboBoxItem selectedItem = MainWindowCombo.SelectedItem as ComboBoxItem;

            if (manageCities.ShowDialog() == true)
            {
                MainWindowCombo.Items.Clear();
                foreach(ListBoxItem item in manageCities.ManageCitiesList.Items)
                {
                    ComboBoxItem comboBoxItem = new ComboBoxItem();
                    comboBoxItem.Content = item.Content;
                    MainWindowCombo.Items.Add(comboBoxItem);
                }
                SetComboItem(selectedItem);
            }
        }
    }
}
