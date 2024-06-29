using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WeatherConnectorLib;

namespace MVC
{
    public class Model
    {

        private string _city;
        public delegate void ModelUpdatedDelegate(WeatherData data);
        public event ModelUpdatedDelegate ModelUpdated;

        public Model()
        {
            WeatherConnector.ApiKey = "";
        }

        public void SetCity(string city)
        {
            _city = city;
            ModelUpdated?.Invoke(Weather);
        }

        public WeatherData Weather
        {
            get { return WeatherConnector.GetWeatherForCity(_city); }
        }
    }
}
