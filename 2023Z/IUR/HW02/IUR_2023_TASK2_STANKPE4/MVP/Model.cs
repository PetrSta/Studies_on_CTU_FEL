using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WeatherConnectorLib;

namespace MVP
{
    public class Model
    {
        public event ModelUpdatedDelegate ModelUpdated;
        public delegate void ModelUpdatedDelegate(WeatherData data);

        private string _city;
        private string _language;
        private string _units;

        public Model()
        {
            WeatherConnector.ApiKey = "";
        }

        public void SetParameters(string city, string language, string units)
        {
            _city = city;
            _language = language;
            _units = units;
            ModelUpdated?.Invoke(WeatherConnector.GetWeatherForCity(_city, _language, _units));
        }

        public WeatherData Weather
        {
            get { return WeatherConnector.GetWeatherForCity(_city, _language, _units); }
        }
    }
}
