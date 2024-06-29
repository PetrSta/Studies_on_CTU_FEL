using System;

namespace MVP
{
    internal class Presenter
    {
        private Model _model;
        private IView _currentView;
        private IView _viewEN;
        private IView _viewCZ;
        private IView _speechView;
        private string _units = string.Empty;
        private string _language;
        private string _city;

        public Presenter(Model model, IView viewEN, IView viewCZ, IView speechView)
        {
            _model = model;
            _viewEN = viewEN;
            _viewCZ = viewCZ;
            _speechView = speechView;
            // default is en
            _currentView = _viewEN;

            _model.ModelUpdated += Model_ModelUpdated;
        }

        private void Model_ModelUpdated(WeatherConnectorLib.WeatherData data)
        {
            _currentView.City = data.City;
            _currentView.Country = data.Country;
            _currentView.Temp = data.Temp;
            _currentView.TempMax = data.TempMax;
            _currentView.TempMin = data.TempMin;
            _currentView.Humidity = data.Humidity;

            _currentView.Render();
        }

        public void ProcessInput()
        {
            while (true)
            {
                // get the command, if language is to be changed call GetLanguage if units are to be changed call GetUnits
                // if language changes set _view to coresponding view
                string command = _currentView.GetCommand();
                
                if(command == "l") 
                { 
                    string language = _currentView.GetLanguage();
                    _language = language;
                    _currentView = _viewEN;
                    if(language == "cz")
                    {
                        _currentView = _viewCZ;
                    }
                    else if(language == "sen")
                    {
                        _currentView = _speechView;
                    }
                    if(_units != string.Empty)
                    {
                        _currentView.SetUnitsSymbol(_units, false);
                    }
                } 
                else if(command == "u")
                {
                    _units = _currentView.GetUnits();
                } 
                else
                {
                    _city = command;
                    ParseCommand(_city, _language, _units);
                }
            }
        }
                
        internal void ParseCommand(string city, string language, string units)
        {
            _model.SetParameters(city, language, units);
        }
    }
}