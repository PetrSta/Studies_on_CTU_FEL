// See https://aka.ms/new-console-template for more information
using MVP;
IView viewEN = new ConsoleViewEN();
IView viewCZ = new ConsoleViewCZ();
IView speechView = new SpeechViewEN();
Model model = new Model();

Presenter presenter = new Presenter(model, viewEN, viewCZ, speechView);

presenter.ProcessInput();
