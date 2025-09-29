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
<<<<<<< HEAD
            WeatherConnector.ApiKey = "a7566150764dc86ea1ff84da03130ca2";
=======
            WeatherConnector.ApiKey = "";
>>>>>>> c5e9c5389d2eb83b79f06777e62ec6551949372f
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
